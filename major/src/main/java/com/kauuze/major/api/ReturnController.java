package com.kauuze.major.api;

import com.jiwuzao.common.domain.mysql.entity.ReturnOrder;
import com.jiwuzao.common.dto.express.ExpressResultDto;
import com.jiwuzao.common.include.JsonResult;
import com.jiwuzao.common.include.PageDto;
import com.jiwuzao.common.pojo.main.PagePojo;
import com.jiwuzao.common.pojo.order.ConfirmReturnPojo;
import com.jiwuzao.common.pojo.order.GoodsReturnPojo;
import com.jiwuzao.common.pojo.order.ReturnPojo;
import com.jiwuzao.common.pojo.order.SendReturnPojo;
import com.jiwuzao.common.vo.order.ExpressResultVO;
import com.jiwuzao.common.vo.order.ReturnOrderVO;
import com.kauuze.major.config.permission.Authorization;
import com.kauuze.major.config.permission.Cms;
import com.kauuze.major.config.permission.Merchant;
import com.kauuze.major.service.ExpressService;
import com.kauuze.major.service.ReturnService;
import com.qiniu.util.Json;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("/return")
@Slf4j
@Transactional
public class ReturnController {

    @Resource
    private ReturnService returnService;
    @Resource
    private ExpressService expressService;

    @RequestMapping("/askGoodsReturn")
    @Authorization
    public JsonResult askGoodsReturn(@RequestAttribute int uid, @RequestBody GoodsReturnPojo pojo) {
        log.info("入参{}", pojo);
        ReturnOrder returnOrder = returnService.askGoodsReturn(uid, pojo.getGoodsOrderNo(), pojo.getReturnContent(), pojo.getImage());
        if (returnOrder != null) {
            return JsonResult.success(returnOrder);
        } else {
            return JsonResult.failure();
        }
    }

    @RequestMapping("/getReturnList")
    @Authorization
    public JsonResult getReturnList(@RequestAttribute int uid, @Valid @RequestBody PagePojo pojo) {
        PageDto<ReturnOrderVO> pageDto = returnService.getUserReturn(uid, pojo.getCurrentPage(), pojo.getPageSize());
        return JsonResult.success(pageDto);
    }

    @RequestMapping("/getReturnListByStore")
    @Merchant
    public JsonResult getReturnListByStore(@RequestAttribute int uid, @Valid @RequestBody PagePojo pojo) {
        PageDto<ReturnOrderVO> pageDto = returnService.getReturnListByStore(uid, pojo.getCurrentPage(), pojo.getPageSize());
        return JsonResult.success(pageDto);
    }

    @RequestMapping("/allowReturn")
    @Merchant
    public JsonResult confirmReturn(@RequestBody @Valid ReturnPojo pojo) {
        ReturnOrder returnOrder = returnService.allowReturnOrder(pojo.getId());
        if (returnOrder != null) {
            return JsonResult.success(returnOrder);
        } else {
            return JsonResult.failure();
        }
    }

    @RequestMapping("/refuseReturn")
    @Merchant
    public JsonResult refuseReturn(@RequestBody @Valid ReturnPojo pojo) {
        ReturnOrder returnOrder = returnService.refuseReturnOrder(pojo.getId(), pojo.getReason());
        if (returnOrder != null) {
            return JsonResult.success(returnOrder);
        } else {
            return JsonResult.failure();
        }
    }

    @RequestMapping("/sendReturnOrder")
    @Authorization
    public JsonResult sendReturnOrder(@RequestBody @Valid SendReturnPojo pojo) {
        ReturnOrder returnOrder = returnService.sendReturnOrder(pojo.getId(), pojo.getExpCode(), pojo.getExpNo());
        if (returnOrder != null) {
            return JsonResult.success(returnOrder);
        } else {
            return JsonResult.failure();
        }
    }

    @RequestMapping("/queryReturnExpress")
    @Authorization
    public JsonResult queryReturnExpress(@RequestBody @Valid ReturnPojo pojo) throws Exception {
        ExpressResultVO expressResultVO = expressService.queryReturnExpress(pojo.getId());
        return JsonResult.success(expressResultVO);
    }

    @RequestMapping("/confirmReceive")
    @Merchant
    public JsonResult confirmReceive(@RequestBody @Valid ReturnPojo pojo) {
        ReturnOrder returnOrder = returnService.confirmReceive(pojo.getId());
        if (returnOrder != null) {
            return JsonResult.success(returnOrder);
        } else {
            return JsonResult.failure();
        }
    }

    @RequestMapping("/confirmReturnOrder")
    @Cms
    public JsonResult confirmReturnOrder(@RequestAttribute int uid, @Valid @RequestBody ConfirmReturnPojo pojo) {
        ReturnOrder returnOrder = returnService.confirmReturnOrder(pojo.getId(), uid, pojo.getPassword());
        if (returnOrder != null) {
            return JsonResult.success(returnOrder);
        } else {
            return JsonResult.failure();
        }
    }
}
