package com.kauuze.major.api;

import com.jiwuzao.common.domain.mongo.entity.RecommendGoods;
import com.jiwuzao.common.domain.mongo.entity.RecommendStore;
import com.jiwuzao.common.include.JsonResult;
import com.jiwuzao.common.pojo.goods.*;
import com.jiwuzao.common.pojo.store.StoreRecommendPojo;
import com.jiwuzao.common.vo.goods.GoodsSimpleVO;
import com.kauuze.major.config.permission.Cms;
import com.kauuze.major.service.RecommendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/recommend")
@Slf4j
public class RecommendController {

    @Autowired
    private RecommendService recommendService;

    /**
     * 每个类目推荐一件商品
     *
     * @param pojo
     * @return
     */
    @RequestMapping("/goodsByCategory")
    public JsonResult goodsByCategory(@RequestBody @Valid GoodsClassifyPojo pojo) {
        GoodsSimpleVO goodsSimpleVO = recommendService.getSimpleByCategory(pojo.getGoodsClassify());
        return JsonResult.success(goodsSimpleVO);
    }

    /**
     * 获取头部推荐商品swiper list
     *
     * @return
     */
    @RequestMapping("/getHeadGoodsList")
    public JsonResult getHeadGoodsList() {
        //todo 商品自定义推荐
        List<GoodsSimpleVO> list = recommendService.getHeadGoodsListTemp();
        return JsonResult.success(list);
    }

    /**
     * 相似极物
     *
     * @param pojo
     * @return
     */
    @RequestMapping("/goodsSimilar")
    public JsonResult getGoodsSimilar(@RequestBody @Valid GoodsItemPojo pojo) {
        List<GoodsSimpleVO> list = recommendService.getGoodsSimilar(pojo.getGoodsId());
        return JsonResult.success(list);
    }

    /**
     * 生成一个推荐商品
     * @param pojo
     * @return
     */
    @RequestMapping("/saveOneGoods")
    @Cms
    public JsonResult createOneGoods(@RequestBody @Valid GoodsRecommendPojo pojo) {
        RecommendGoods recommendGoods = recommendService.saveOne(pojo.getGoodsId(), pojo.getReason());
        if (null != recommendGoods) {
            return JsonResult.success(recommendGoods);
        } else {
            return JsonResult.failure();
        }
    }

    /**
     * 后台内容管理员创建一个推荐店铺
     *
     * @param pojo
     * @return
     */
    @RequestMapping("/createRecommendStore")
    @Cms
    public JsonResult createRecommendStore(@RequestBody @Valid StoreRecommendPojo pojo) {
        RecommendStore recommendStore = recommendService.addRecommendStore(pojo.getStoreId(), pojo.getImages().split(","));
        if (null != recommendStore) {
            return JsonResult.success(recommendStore);
        } else {
            return JsonResult.failure();
        }
    }

    /**
     * jijiang获取推荐店铺
     * @return
     */
    @RequestMapping("/getRecommendStore")
    public JsonResult getRecommendStore() {
        RecommendStore latestRecommendStore = recommendService.getLatestRecommendStore();
        if (null != latestRecommendStore) {
            return JsonResult.success(latestRecommendStore);
        }else{
            return JsonResult.failure();
        }
    }
}
