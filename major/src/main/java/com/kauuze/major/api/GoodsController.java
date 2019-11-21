package com.kauuze.major.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jiwuzao.common.domain.mongo.entity.Category;
import com.jiwuzao.common.domain.mongo.entity.Goods;
import com.jiwuzao.common.domain.mongo.entity.GoodsSpec;
import com.jiwuzao.common.dto.goods.GoodsSimpleDto;
import com.jiwuzao.common.include.JsonResult;
import com.jiwuzao.common.include.JsonUtil;
import com.jiwuzao.common.include.PageDto;
import com.jiwuzao.common.include.StringUtil;
import com.jiwuzao.common.pojo.common.*;
import com.jiwuzao.common.pojo.goods.*;
import com.jiwuzao.common.pojo.store.StorePojo;
import com.jiwuzao.common.vo.goods.*;
import com.jiwuzao.common.vo.user.AppriseVO;
import com.kauuze.major.api.pojo.GetGoodsDetailPojo;
import com.kauuze.major.config.contain.ParamMismatchException;
import com.kauuze.major.config.permission.Authorization;
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
        String result = goodsService.addGoods(uid, addGoodsPojo.getGoodsClassify(), addGoodsPojo.getTitle(), addGoodsPojo.getCover(), addGoodsPojo.getDefaultPrice(), addGoodsPojo.getSlideshow(), addGoodsPojo.getPostage(), addGoodsPojo.getDetailLabel(), addGoodsPojo.getGoodsType(), addGoodsPojo.getGoodsTypeClass(), addGoodsPojo.getDetailPhotos(), addGoodsPojo.getGoodsSpecPojo(), addGoodsPojo.getGoodsReturn(), addGoodsPojo.getDeliveryTime(),addGoodsPojo.getGoodsSecondClassify(), addGoodsPojo.getGoodsThirdClassify());
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
     * 商品下架
     *
     * @param uid
     * @param gidPojo
     * @return
     */
    @RequestMapping("/putOff")
    @Merchant
    public JsonResult putOff(@RequestAttribute int uid, @Valid @RequestBody GidPojo gidPojo) {
        String result = goodsService.putOff(uid, gidPojo.getGid());
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
        if (!page.getContent().isEmpty())
            return JsonResult.success(page);
        return JsonResult.failure("没找到商品信息");
    }

    /**
     * 商户查询某审核状态下的商品信息
     * @param uid 用户id
     * @param goodsPagePojo 商品分页信息
     * @return
     */
    @RequestMapping("/getGoodsListByStatus")
    @Merchant
    public JsonResult getGoodsListByStatus(@RequestAttribute int uid,@Valid @RequestBody GoodsPagePojo goodsPagePojo){
        Pageable pageAble;
        if (goodsPagePojo.getIsAsc()) {
            pageAble = PageRequest.of(goodsPagePojo.getCurrentPage(), goodsPagePojo.getPageSize(), Sort.Direction.ASC, goodsPagePojo.getSortBy());
        } else {
            pageAble = PageRequest.of(goodsPagePojo.getCurrentPage(), goodsPagePojo.getPageSize(), Sort.Direction.DESC, goodsPagePojo.getSortBy());
        }
        PageDto<GoodsSimpleDto> pageDto = goodsService.getGoodsListStatus(uid,goodsPagePojo.getAuditType(),pageAble);
        return JsonResult.success(pageDto);
    }

    @RequestMapping("/getGoodsListByPutAway")
    @Merchant
    public JsonResult getGoodsListByPutAway(@RequestAttribute int uid,@Valid @RequestBody GoodsPagePojo goodsPagePojo){
        Pageable pageAble;
        if (goodsPagePojo.getIsAsc()) {
            pageAble = PageRequest.of(goodsPagePojo.getCurrentPage(), goodsPagePojo.getPageSize(), Sort.Direction.ASC, goodsPagePojo.getSortBy());
        } else {
            pageAble = PageRequest.of(goodsPagePojo.getCurrentPage(), goodsPagePojo.getPageSize(), Sort.Direction.DESC, goodsPagePojo.getSortBy());
        }
        PageDto<GoodsSimpleDto> pageDto = goodsService.getGoodsListByPutAway(uid,goodsPagePojo.getPutaway(),pageAble);
        return JsonResult.success(pageDto);
    }

    @RequestMapping("/merchantGetGoodsDetail")
    @Merchant
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
     * @param
     * @return
     */
    @JsonIgnore
    @JsonProperty(value = "uid")
    @RequestMapping("/getGoodsDetail")
    public JsonResult getGoodsDetailApp(@Valid @RequestBody GetGoodsDetailPojo pojo) {
        log.debug("uid:" + pojo.getGid());
        //游客访问时uid传-1
        int uid = -1;
        if (pojo.getUid() != null)
            uid = pojo.getUid();
        GoodsDetailVO vo = goodsService.getGoodsDetail(pojo.getGid(), uid);
        if (vo == null) {
            return JsonResult.failure("没有这件商品");
        } else {
            return JsonResult.success(vo);
        }
    }

    /**
     * 获取商品评论
     *
     * @param gidPojo
     * @return
     */
    @RequestMapping("/getGoodsComment")
    public JsonResult getGoodsComment(@Valid @RequestBody GidPojo gidPojo) {
        List<GoodsCommentVO> volist = goodsService.getGoodsComment(gidPojo.getGid());
        if (volist == null) {
            return JsonResult.failure();
        } else {
            return JsonResult.success(volist);
        }
    }

    /**
     * 获取商品规格信息
     *
     * @param goodsSpecPojo
     * @return
     */
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
    public JsonResult getGoodsByClassify(@RequestBody GoodsPagePojo goodsPagePojo) {
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

    /**
     * 
     * @param uid
     * @param pojo
     * @return
     */
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
     * putAway:上架
     * @param pojo
     * @return
     */
    @RequestMapping("/getGoodsByStore")
    public JsonResult getGoodsByStore(@Valid @RequestBody StorePojo pojo) {
        PageDto<GoodsSimpleVO> page = goodsService.getGoodsByStore(pojo.getStoreId(),true, PageRequest.of(pojo.getPageNum(), pojo.getPageSize(), Sort.by(pojo.getOrderBy())));
        return JsonResult.success(page);
    }

    /**
     * 添加商品评论
     */
    @RequestMapping("/addComment")
    @Authorization
    public JsonResult addComment(@RequestAttribute int uid, @Valid @RequestBody CommentPojo pojo) {
        String s = goodsService.addComment(uid, pojo.getGoodsOrderNo(), pojo.getComment(),pojo.getStar());
        if (s == null) {
            return JsonResult.failure();
        } else {
            return JsonResult.success(s);
        }
    }

    /**
     * 删除商品评论
     */
    @RequestMapping("/delComment")
    @Authorization
    public JsonResult delComment(@RequestAttribute int uid, @Valid @RequestBody String comid) {
        String s = goodsService.delComment(uid, comid);
        if (StringUtil.isBlank(s)) {
            return JsonResult.failure();
        } else {
            return JsonResult.success(s);
        }
    }

    /**
     * 为商品点赞，用户不能重复点赞
     */
    @RequestMapping("/addApprise")
    @Authorization
    public JsonResult addApprise(@RequestAttribute int uid, @Valid @RequestBody GidPojo pojo) {
        Long appriseCnt = goodsService.addApprise(uid, pojo.getGid());
        if (appriseCnt != null) {
            return JsonResult.success(appriseCnt);
        } else {
            return JsonResult.failure("点赞失败");
        }
    }

    /**
     * 取消点赞
     *
     * @param uid
     * @param pojo
     * @return
     */
    @RequestMapping("/delApprise")
    @Authorization
    public JsonResult delApprise(@RequestAttribute int uid, @Valid @RequestBody GidPojo pojo) {
        Long appriseCnt = goodsService.delApprise(uid, pojo.getGid());
        if (appriseCnt != null) {
            return JsonResult.success(appriseCnt);
        } else {
            return JsonResult.failure("取消失败");
        }
    }

    /**
     * 获取用户点赞列表
     */
    @RequestMapping("/getAppriseList")
    public JsonResult getAppriseList(@Valid @RequestBody UidPojo pojo) {
        List<AppriseVO> list= goodsService.getAppriseList(pojo.getUid());
        if (list != null) {
            return JsonResult.success(list);
        } else {
            return JsonResult.failure("获取失败");
        }
    }

    /**
     * 获取浏览记录
     */
    @RequestMapping("getViewHistory")
    @Authorization
    public JsonResult getViewHistory(@RequestAttribute int uid) {
        List<ViewHistoryVO> list = goodsService.getViewHistory(uid);
        if (list.size() == 0) {
            return JsonResult.success("列表为空");
        } else {
            return JsonResult.success(list);
        }
    }

    /**
     * 添加浏览记录
     */
    @RequestMapping("addViewHistory")
    @Authorization
    public JsonResult addViewHistory(@RequestAttribute int uid, @Valid @RequestBody GidPojo pojo) {
        String msg = goodsService.addViewHistory(uid, pojo.getGid());
        if (msg != null) {
            return JsonResult.success(msg);
        } else {
            return JsonResult.failure("记录失败");
        }
    }

    /**
     * 删除浏览记录
     */
    @RequestMapping("delViewHistory")
    @Authorization
    public JsonResult delViewHistory(@RequestAttribute int uid, @Valid @RequestBody VhidPojo pojo) {
        //这个pojo传历史vhid
        String msg = goodsService.delViewHistory(uid, pojo.getVhid());
        if (msg != null) {
            return JsonResult.success(msg);
        } else {
            return JsonResult.failure("删除失败");
        }
    }

}
