package com.kauuze.major.service;

import com.jiwuzao.common.domain.enumType.GoodsClassifyEnum;
import com.jiwuzao.common.domain.mongo.entity.Category;
import com.jiwuzao.common.domain.mongo.entity.Goods;
import com.jiwuzao.common.domain.mongo.entity.RecommendGoods;
import com.jiwuzao.common.domain.mongo.entity.RecommendStore;
import com.jiwuzao.common.domain.mongo.entity.userBastic.Store;
import com.jiwuzao.common.exception.OrderException;
import com.jiwuzao.common.exception.StoreException;
import com.jiwuzao.common.exception.excEnum.StoreExceptionEnum;
import com.jiwuzao.common.vo.goods.GoodsSimpleVO;
import com.kauuze.major.domain.mongo.repository.*;
import org.omg.SendingContext.RunTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendService {

    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private GoodsDetailRepository goodsDetailRepository;
    @Autowired
    private RecommendGoodsRepository recommendGoodsRepository;
    @Autowired
    private RecommendStoreRepository recommendStoreRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private StoreRepository storeRepository;


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
     *
     * @param goodsClassify
     * @return
     */
    public RecommendGoods getOne(GoodsClassifyEnum goodsClassify) {
        return recommendGoodsRepository.findByGoodsClassifyAndStatus(goodsClassify, true).orElse(null);
    }

    public GoodsSimpleVO getSimpleByCategory(GoodsClassifyEnum goodsClassify) {
        Goods goods = goodsRepository.findByClassifyAndPutaway(PageRequest.of(0, 10), goodsClassify, true).get(0);
        return new GoodsSimpleVO().setGoodsPrice(goods.getDefaultPrice())
                .setGoodsName(goods.getTitle()).setGoodsImg(goods.getCover()).setGoodsId(goods.getGid());
    }

    public List<GoodsSimpleVO> getHeadGoodsList() {
        //todo 商品列表头部推荐算法,暂时取极物造店铺中前五个
        List<Goods> list = goodsRepository.findAllBySidAndPutaway("5d639c11d6018000015e1865", true);
        return list.stream().sorted(Comparator.comparingLong(Goods::getCreateTime).reversed()).map(goods -> new GoodsSimpleVO().setGoodsId(goods.getGid()).setGoodsImg(goodsDetailRepository.findByGid(goods.getGid()).get().getSlideshow().split(",")[0]).setGoodsName(goods.getTitle()).setGoodsPrice(goods.getDefaultPrice()))
                .limit(5L).collect(Collectors.toList());
    }

    public RecommendStore addRecommendStore(String storeId, String... images) {
        Optional<Store> optional = storeRepository.findById(storeId);
        if (!optional.isPresent()) throw new StoreException(StoreExceptionEnum.STORE_NOT_FOUND);
        Store store = optional.get();
        List<String> list = new ArrayList<>();
        Collections.addAll(list, images);
        RecommendStore recommendStore = new RecommendStore().setStoreId(storeId).setStoreName(store.getStoreName())
                .setCreateTime(System.currentTimeMillis()).setImages(list);
        return recommendStoreRepository.save(recommendStore);
    }

    public RecommendStore getLatestRecommendStore() {
        return recommendStoreRepository.findAll().stream().max(Comparator.comparingLong(RecommendStore::getCreateTime)).orElse(
                new RecommendStore().setStoreId("").setImages(new ArrayList<>())
        );
    }

}
