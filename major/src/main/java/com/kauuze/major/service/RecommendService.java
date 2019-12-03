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
import com.jiwuzao.common.include.PageUtil;
import com.jiwuzao.common.vo.goods.GoodsSimpleVO;
import com.kauuze.major.domain.mongo.repository.*;
import com.kauuze.major.include.PageDto;
import org.apache.logging.log4j.util.Strings;
import org.omg.SendingContext.RunTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
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

    public RecommendGoods saveOne(String gids, String reason) {
        String[] strings = gids.split(",");
        List<String> list = new ArrayList<>();
        Collections.addAll(list, strings);
        return recommendGoodsRepository.save(new RecommendGoods().setGids(list).setReason(reason).setStatus(true).setCreateTime(System.currentTimeMillis()));
    }

    public PageDto<GoodsSimpleVO> showRecommendByStore(String storeId,Integer num,Integer size){
        Pageable pageable = PageUtil.getNewsInsert(num, size);
        Page<Goods> page = goodsRepository.findAllBySid(storeId, pageable);
        page.filter(Goods::getPutaway)
                .map(goods -> new GoodsSimpleVO().setGoodsImg(goods.getCover())
                        .setGoodsName(goods.getTitle()).setGoodsId(goods.getGid()));
        System.out.println(page.getContent());
        return null;
    }

    public List<GoodsSimpleVO> getRecommendNew() {
        return recommendGoodsRepository.findAll().stream()
                .filter(RecommendGoods::getStatus).max(Comparator.comparingLong(RecommendGoods::getCreateTime))
                .orElseThrow(() -> new RuntimeException("无推荐")).getGids()
                .stream().filter(Strings::isNotBlank)
                .map(gid -> {
                    Goods goods = goodsRepository.findById(gid).orElseThrow(() -> new RuntimeException("无推荐商品"));
                    return createGoodsSimple(goods);
                }).collect(Collectors.toList());
    }

    private GoodsSimpleVO createGoodsSimple(Goods goods) {
        return new GoodsSimpleVO().setGoodsName(goods.getTitle()).setGoodsId(goods.getGid()).setGoodsPrice(goods.getDefaultPrice()).setGoodsImg(goods.getCover());
    }

    public GoodsSimpleVO getSimpleByCategory(GoodsClassifyEnum goodsClassify) {
        Goods goods = goodsRepository.findByClassifyAndPutaway(PageRequest.of(0, 10), goodsClassify, true).get(0);
        return new GoodsSimpleVO().setGoodsPrice(goods.getDefaultPrice())
                .setGoodsName(goods.getTitle()).setGoodsImg(goods.getCover()).setGoodsId(goods.getGid());
    }

    public List<GoodsSimpleVO> getHeadGoodsList() {
        //todo 商品列表头部推荐算法,暂时取极物造店铺中前五个
        List<Goods> list = goodsRepository.findAllBySidAndPutaway("5d639c11d6018000015e1865", true);
        return list.stream().sorted(Comparator.comparingLong(Goods::getCreateTime).reversed())
                .map(goods -> new GoodsSimpleVO().setGoodsId(goods.getGid()).setGoodsImg(goodsDetailRepository.findByGid(goods.getGid()).get().getSlideshow().split(",")[0]).setGoodsName(goods.getTitle()).setGoodsPrice(goods.getDefaultPrice()))
                .limit(5L).collect(Collectors.toList());
    }

    public List<GoodsSimpleVO> getHeadGoodsListTemp(){
        List<GoodsSimpleVO> list = new ArrayList<>();
        GoodsSimpleVO vo1 = new GoodsSimpleVO().setGoodsId("5dcaaa5ed60180000103b55b").setGoodsImg("http://cdn.jiwuzao.com/8586430e554f40ff81c27aafdd949de1.jpg");
        GoodsSimpleVO vo2 = new GoodsSimpleVO().setGoodsId("5dcaaba6d60180000103b581").setGoodsImg("http://cdn.jiwuzao.com/3c4ca03202dd4738a7c0a43ff84376bc.jpg");
        GoodsSimpleVO vo3 = new GoodsSimpleVO().setGoodsId("5dda98f1d60180000135cfe7").setGoodsImg("http://cdn.jiwuzao.com/200c8435b4294c6a8c64f029b73244e6.jpg");
        GoodsSimpleVO vo4 = new GoodsSimpleVO().setGoodsId("5de4c021d601800001c4d293").setGoodsImg("http://cdn.jiwuzao.com/995a7c68afc94a9ab528461691ce38e3.jpg");
        GoodsSimpleVO vo5 = new GoodsSimpleVO().setGoodsId("5de5db57d601800001c4de00").setGoodsImg("http://cdn.jiwuzao.com/91c8a8c751d54526916595eef4362595.jpg");
        GoodsSimpleVO vo6 = new GoodsSimpleVO().setGoodsId("5de5d24dd601800001c4d94f").setGoodsImg("http://cdn.jiwuzao.com/713ee8f0be004f7c8e9caa146a8aaa7f.jpg");
        list.add(vo1);
        list.add(vo2);
        list.add(vo3);
        list.add(vo4);
        list.add(vo5);
        list.add(vo6);
        return list;


    }

    /**
     * 增加一个极匠推荐店铺。
     *
     * @param storeId 店铺id
     * @param images  图片数组
     * @return RecommendStore
     */
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

    /**
     * 获取一个推荐店铺
     *
     * @return 推荐
     */
    public RecommendStore getLatestRecommendStore() {
        ArrayList<String> images = new ArrayList<>();
        images.add("http://cdn.jiwuzao.com/image/jijiang/MasterMap.png");//todo 默认极物造页面
        return recommendStoreRepository.findAll().stream().max(Comparator.comparingLong(RecommendStore::getCreateTime)).orElse(
                new RecommendStore().setStoreId("5d639c11d6018000015e1865").setImages(images)
        );
    }


}
