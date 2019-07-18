package com.kauuze.manager.api;

import com.jiwuzao.common.dto.goods.GoodsOpenDto;
import com.jiwuzao.common.pojo.common.AuditGoodsPojo;
import com.jiwuzao.common.pojo.common.AuditTypePojo;
import com.jiwuzao.common.pojo.common.PagePojo;
import com.kauuze.manager.config.permission.Cms;
import com.kauuze.manager.include.JsonResult;
import com.kauuze.manager.include.PageDto;
import com.kauuze.manager.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    /**
     * 按认证状态分页查询商品
     * @param auditTypePojo
     * @return
     */
    @RequestMapping("/findGoodsByAuditType")
    @Cms
    public JsonResult findGoodsByAuditType(@Valid @RequestBody AuditTypePojo auditTypePojo){
        PagePojo pagePojo = auditTypePojo.getPage();
        PageDto<GoodsOpenDto> pageDto = goodsService.findGoodsByAuditType(auditTypePojo.getAuditType(),
                pagePojo.getNum(), 20);
        if (pageDto.getTotal() != 0)
            return JsonResult.success(pageDto);
        return JsonResult.failure("没找到商品信息");
    }

    /**
     * 改变认证状态
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