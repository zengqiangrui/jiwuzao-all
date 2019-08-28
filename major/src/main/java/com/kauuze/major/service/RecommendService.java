package com.kauuze.major.service;

import com.jiwuzao.common.domain.enumType.GoodsClassifyEnum;
import com.jiwuzao.common.domain.mongo.entity.Goods;
import com.jiwuzao.common.domain.mongo.entity.RecommendGoods;
import com.jiwuzao.common.vo.goods.GoodsSimpleVO;
import com.kauuze.major.domain.mongo.repository.GoodsDetailRepository;
import com.kauuze.major.domain.mongo.repository.GoodsRepository;
import com.kauuze.major.domain.mongo.repository.RecommendGoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class RecommendService {

    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private GoodsDetailRepository goodsDetailRepository;
    @Autowired
    private RecommendGoodsRepository recommendGoodsRepository;


    public List<GoodsSimpleVO> getGoodsSimilar(String goodsId) {
        Optional<Goods> byId = goodsRepository.findById(goodsId);
        if (!byId.isPresent()) {
            throw new RuntimeException("商品不存在");
        }
        //todo 产生一个商品推荐list的算法，暂时用相同店铺下的商品进行推荐
        Goods goods = byId.get();
        List<Goods> list = goodsRepository.findAllBySidAndPutaway(goods.getSid(), true);
        //推荐6个商品
        List<GoodsSimpleVO> goodsSimpleVOS = new ArrayList<>();
        for (Goods item : list) {
            if (item.getGid().equals(goodsId)) continue;
            GoodsSimpleVO goodsSimpleVO = new GoodsSimpleVO();
            goodsSimpleVO.setGoodsId(item.getGid()).setGoodsName(item.getTitle())
                    .setGoodsImg(item.getCover()).setGoodsPrice(item.getDefaultPrice());
            if (!goodsSimpleVOS.contains(goodsSimpleVO) && goodsSimpleVOS.size() < 6) {
                goodsSimpleVOS.add(goodsSimpleVO);
            }
        }
        return goodsSimpleVOS;
    }

    public RecommendGoods saveOne(String goodsId, String reason) {
        Optional<Goods> optional = goodsRepository.findById(goodsId);
        if (!optional.isPresent()) throw new RuntimeException("商品信息不存在");
        Goods goods = optional.get();
        Optional<RecommendGoods> optional1 = recommendGoodsRepository.findByGoodsClassifyAndStatus(goods.getClassify(), true);
        RecommendGoods recommendGoods = new RecommendGoods();
        recommendGoods.setStatus(true).setCover(goods.getCover()).setGoodsClassify(goods.getClassify())
                .setGoodsId(goods.getGid()).setPrice(goods.getDefaultPrice()).setReason(reason).setCreateTime(System.currentTimeMillis());
        if (optional1.isPresent()) {
            RecommendGoods recommendGoods1 = optional1.get();
            recommendGoods1.setStatus(false);
            recommendGoodsRepository.save(recommendGoods1);
        }
        return recommendGoodsRepository.save(recommendGoods);
    }

    /**
     * 根据主分类获取一个推荐
     * @param goodsClassify
     * @return
     */
    public RecommendGoods getOne(GoodsClassifyEnum goodsClassify){
        return recommendGoodsRepository.findByGoodsClassifyAndStatus(goodsClassify,true).orElse(null);
    }
}
