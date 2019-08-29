package com.kauuze.major.api;

import com.jiwuzao.common.domain.mongo.entity.RecommendGoods;
import com.jiwuzao.common.include.JsonResult;
import com.jiwuzao.common.pojo.goods.*;
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

    @RequestMapping("/getHeadGoodsList")
    public JsonResult getHeadGoodsList(){
        List<GoodsSimpleVO> list = recommendService.getHeadGoodsList();
        return JsonResult.success(list);
    }

    @RequestMapping("/goodsSimilar")
    public JsonResult getGoodsSimilar(@RequestBody @Valid GoodsItemPojo pojo) {
        List<GoodsSimpleVO> list = recommendService.getGoodsSimilar(pojo.getGoodsId());
        return JsonResult.success(list);
    }

    @RequestMapping("/getByClassify")
    public JsonResult getByClassfy(@RequestBody @Valid GoodsClassifyPojo pojo) {
        RecommendGoods one = recommendService.getOne(pojo.getGoodsClassify());
        if (null != one) {
            return JsonResult.success(one);
        } else {
            return JsonResult.failure();
        }
    }

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

}
