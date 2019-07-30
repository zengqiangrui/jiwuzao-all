package com.kauuze.major.api;

import com.jiwuzao.common.domain.mongo.entity.GoodsSpec;
import com.jiwuzao.common.dto.goods.GoodsSimpleDto;
import com.jiwuzao.common.pojo.common.GidPojo;
import com.jiwuzao.common.pojo.common.GoodsSpecPojo;
import com.jiwuzao.common.pojo.common.QuerySpecPojo;
import com.jiwuzao.common.pojo.goods.AddGoodsPojo;
import com.jiwuzao.common.pojo.goods.CategoryPojo;
import com.jiwuzao.common.pojo.goods.GoodsPagePojo;
import com.kauuze.major.config.contain.ParamMismatchException;
import com.kauuze.major.config.permission.Merchant;
import com.jiwuzao.common.domain.mongo.entity.Category;
import com.jiwuzao.common.include.JsonResult;
import com.jiwuzao.common.include.JsonUtil;
import com.jiwuzao.common.include.PageDto;
import com.kauuze.major.service.GoodsService;
import com.jiwuzao.common.dto.goods.GoodsOpenDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-29 15:49
 */
@RestController
@RequestMapping("/goods")
@Slf4j
public class GoodsController {
    @Autowired
    private GoodsService goodsService;


    /**
     * 添加商品
     *
     * @param uid
     * @param addGoodsPojo
     * @return
     */
    @RequestMapping("/addGoods")
    @Merchant
    public JsonResult addGoods(@RequestAttribute int uid, @Valid @RequestBody AddGoodsPojo addGoodsPojo) {
        List<Map<String, List<String>>> list = JsonUtil.parseJsonString(addGoodsPojo.getGoodsTypeClass(), List.class);
        int countSpec = 1;
        for (Map<String, List<String>> map : list) {
            for (String s : map.keySet()) {
                List list2 = map.get(s);
                countSpec = list2.size() * countSpec;
            }
        }
        if (countSpec != addGoodsPojo.getGoodsSpecPojo().size()) {
            throw new ParamMismatchException();
        }
        String result = goodsService.addGoods(uid, addGoodsPojo.getGoodsClassify(), addGoodsPojo.getTitle(), addGoodsPojo.getCover(), addGoodsPojo.getDefaultPrice(), addGoodsPojo.getSlideshow(), addGoodsPojo.getPostage(), addGoodsPojo.getDetailLabel(), addGoodsPojo.getGoodsType(), addGoodsPojo.getGoodsTypeClass(), addGoodsPojo.getDetailPhotos(), addGoodsPojo.getGoodsSpecPojo());
        if (result == null) {
            return JsonResult.success();
        } else {
            return JsonResult.failure(result);
        }
    }

    /**
     * 商品上架
     *
     * @param uid
     * @param gidPojo
     * @return
     */
    @RequestMapping("/putaway")
    @Merchant
    public JsonResult putaway(@RequestAttribute int uid, @Valid @RequestBody GidPojo gidPojo) {
        String result = goodsService.putAway(uid, gidPojo.getGid());
        if (result == null) {
            return JsonResult.success();
        } else {
            return JsonResult.failure(result);
        }
    }

    /**
     * 获取商品目录
     *
     * @param categoryPojo
     * @return
     */
    @RequestMapping("/getCategory")
    public JsonResult getCategory(@Valid @RequestBody CategoryPojo categoryPojo) {
        Category category = goodsService.getCategoryById(categoryPojo.getCategoryId());
        if (null != category) {
            return JsonResult.success(category);
        } else {
            return JsonResult.failure();
        }
    }

    /**
     * 获取商品列表分页显示
     *
     * @param goodsPagePojo
     * @return
     */
    @RequestMapping("/getGoodsList")
    @Merchant
    public <T> JsonResult getGoodsList(@RequestAttribute int uid, @Valid @RequestBody GoodsPagePojo goodsPagePojo) {
        Pageable pageAble;
        if (goodsPagePojo.getIsAsc()) {
            pageAble = PageRequest.of(goodsPagePojo.getCurrentPage(), goodsPagePojo.getPageSize(), Sort.Direction.ASC, goodsPagePojo.getSortBy());
        } else {
            pageAble = PageRequest.of(goodsPagePojo.getCurrentPage(), goodsPagePojo.getPageSize(), Sort.Direction.DESC, goodsPagePojo.getSortBy());
        }
        PageDto page;
        if (goodsPagePojo.getIsDetail()) {
            page = goodsService.getGoodsPageByUid(uid, pageAble);
        } else {
            page = goodsService.getGoodsSimple(uid, pageAble);
        }
        if (page.getContent().size() != 0)
            return JsonResult.success(page);
        return JsonResult.failure("没找到商品信息");
    }

    @RequestMapping("/getSpec")
    public JsonResult getSpec(@Valid @RequestBody QuerySpecPojo goodsSpecPojo){
        GoodsSpec goodsSpec =  goodsService.getSpecByGoodsSpecClass(goodsSpecPojo.getGid(),goodsSpecPojo.getSpecClass());
        if(null!=goodsSpec){
            return JsonResult.success(goodsSpec);
        }else{
            return JsonResult.failure("没找到该规格");
        }
    }

}
