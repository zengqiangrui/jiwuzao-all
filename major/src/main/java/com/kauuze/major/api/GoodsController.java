package com.kauuze.major.api;

import com.jiwuzao.common.domain.mongo.entity.Category;
import com.jiwuzao.common.domain.mongo.entity.Goods;
import com.jiwuzao.common.domain.mongo.entity.GoodsSpec;
import com.jiwuzao.common.dto.goods.GoodsSimpleDto;
import com.jiwuzao.common.include.JsonResult;
import com.jiwuzao.common.include.JsonUtil;
import com.jiwuzao.common.include.PageDto;
import com.jiwuzao.common.include.StringUtil;
import com.jiwuzao.common.pojo.common.GidPojo;
import com.jiwuzao.common.pojo.common.QuerySpecPojo;
import com.jiwuzao.common.pojo.goods.*;
import com.jiwuzao.common.pojo.store.StoreIdPojo;
import com.jiwuzao.common.pojo.store.StorePagePojo;
import com.jiwuzao.common.pojo.store.StorePojo;
import com.jiwuzao.common.vo.goods.GoodsDetailVO;
import com.jiwuzao.common.vo.goods.GoodsSimpleVO;
import com.jiwuzao.common.vo.goods.MerchantGoodsVO;
import com.kauuze.major.config.contain.ParamMismatchException;
import com.kauuze.major.config.permission.Merchant;
import com.kauuze.major.service.GoodsService;
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

    @RequestMapping("/merchantGetGoodsDetail")
    public JsonResult getGoodsDetail(@Valid @RequestBody GidPojo gidPojo) {
        MerchantGoodsVO goodsVO = goodsService.merchantGetGoodsDetail(gidPojo.getGid());
        if (null != goodsVO) {
            return JsonResult.success(goodsVO);
        } else {
            return JsonResult.failure("没找到商品信息");
        }
    }

    /**
     * app端获取商品详情
     *
     * @param gidPojo
     * @return
     */
    @RequestMapping("/getGoodsDetail")
    public JsonResult getGoodsDetailApp(@Valid @RequestBody GidPojo gidPojo) {
        log.debug(gidPojo.getGid());
        GoodsDetailVO vo = goodsService.getGoodsDetail(gidPojo.getGid());
        if (vo == null) {
            return JsonResult.failure("没有这件商品");
        } else {
            return JsonResult.success(vo);
        }
    }

    @RequestMapping("/getSpec")
    public JsonResult getSpec(@Valid @RequestBody QuerySpecPojo goodsSpecPojo) {
        GoodsSpec goodsSpec = goodsService.getSpecByGoodsSpecClass(goodsSpecPojo.getGid(), goodsSpecPojo.getSpecClass());
        if (null != goodsSpec) {
            return JsonResult.success(goodsSpec);
        } else {
            return JsonResult.failure("没找到该规格");
        }
    }

    /**
     * 获取极物页面商品列表分页
     *
     * @param goodsPagePojo
     * @return
     */
    @RequestMapping("/getGoodsByClassfy")
    public JsonResult getGoodsByClassfy(@RequestBody GoodsPagePojo goodsPagePojo) {
        System.out.println(goodsPagePojo);
        List<Goods> goodsList = goodsService.getGoodsList(goodsPagePojo);
        if (!goodsList.isEmpty()) {
            return JsonResult.success(goodsList);
        } else {
            return JsonResult.failure();
        }
    }

    @RequestMapping("/modifyPrice")
    @Merchant
    public JsonResult modifyPrice(@RequestAttribute int uid, @Valid @RequestBody GoodsSpecPricePojo pojo) {
        String s = goodsService.modifyPrice(uid, pojo.getGid(), pojo.getGoodsSpecId(), pojo.getGoodsPrice());
        if (StringUtil.isBlank(s)) {
            return JsonResult.success();
        } else {
            return JsonResult.failure(s);
        }
    }

    @RequestMapping("/modifyInventory")
    @Merchant
    public JsonResult modifyInventory(@RequestAttribute int uid, @Valid @RequestBody GoodsSpecInventoryPojo pojo) {
        String s = goodsService.modifyInventory(uid, pojo.getGid(), pojo.getGoodsSpecId(), pojo.getInventory());
        if (StringUtil.isBlank(s)) {
            return JsonResult.success();
        } else {
            return JsonResult.failure(s);
        }
    }

    @RequestMapping("/modifyPostage")
    @Merchant
    public JsonResult modifyPostage(@RequestAttribute int uid, @Valid @RequestBody GoodsPostagePojo pojo) {
        String s = goodsService.modifyPostage(uid, pojo.getGid(), pojo.getPostage());
        if (StringUtil.isBlank(s)) {
            return JsonResult.success();
        } else {
            return JsonResult.failure(s);
        }
    }

    /**
     * 获取店铺商品信息，分页展示
     * @param pojo
     * @return
     */
    @RequestMapping("/getGoodsByStore")
    public JsonResult getGoodsByStore(@Valid @RequestBody StorePojo pojo) {
        PageDto<GoodsSimpleVO> page = goodsService.getGoodsByStore(pojo.getStoreId(), PageRequest.of(pojo.getPageNum(), pojo.getPageSize(), Sort.by(pojo.getOrderBy())));
        return JsonResult.success(page);
    }
}
