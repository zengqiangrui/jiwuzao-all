package com.kauuze.manager.api;

import com.jiwuzao.common.dto.goods.GoodsOpenDto;
import com.jiwuzao.common.dto.goods.GoodsSimpleDto;
import com.jiwuzao.common.include.JsonResult;
import com.jiwuzao.common.pojo.common.AuditGoodsPojo;
import com.jiwuzao.common.pojo.common.AuditTypePojo;
import com.jiwuzao.common.pojo.common.PagePojo;
import com.jiwuzao.common.pojo.goods.GoodsItemPojo;
import com.jiwuzao.common.vo.goods.ManageGoodsVO;
import com.kauuze.manager.config.permission.Cms;
import com.kauuze.manager.include.PageDto;
import com.kauuze.manager.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/goods")
@Slf4j
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    /**
     * 按认证状态分页查询商品
     *
     * @param auditTypePojo
     * @return
     */
    @RequestMapping("/findGoodsByAuditType")
    @Cms
    public JsonResult findGoodsByAuditType(@Valid @RequestBody AuditTypePojo auditTypePojo) {
        PagePojo pagePojo = auditTypePojo.getPage();
        PageDto<GoodsSimpleDto> pageDto = goodsService.findSimpleGoods(auditTypePojo.getAuditType(),
                pagePojo.getNum(), pagePojo.getSize());
        if (pageDto.getTotal() != 0) {
            return JsonResult.success(pageDto);
        }
        return JsonResult.failure("没找到商品信息");
    }

    @RequestMapping("/getGoodsDetail")
    @Cms
    public JsonResult cmsGetGoodsDetail(@RequestBody @Valid GoodsItemPojo pojo) {
        ManageGoodsVO detail = goodsService.cmsGetGoodsDetail(pojo.getGoodsId());
        if (detail != null)
            return JsonResult.success(detail);
        else
            return JsonResult.failure();
    }

    /**
     * 改变认证状态
     *
     * @param auditGoodsPojo
     * @return
     */
    @RequestMapping("/audit")
    @Cms
    public JsonResult audit(@RequestAttribute int uid, @Valid @RequestBody AuditGoodsPojo auditGoodsPojo) {
        String result = goodsService.audit(uid, auditGoodsPojo.getGid(), auditGoodsPojo.getAuditType());
        if (result == null) {
            return JsonResult.success();
        } else {
            return JsonResult.failure(result);
        }
    }


}