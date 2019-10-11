package com.kauuze.major.api;

import com.jiwuzao.common.domain.mysql.entity.ReturnOrder;
import com.jiwuzao.common.include.JsonResult;
import com.jiwuzao.common.include.PageDto;
import com.jiwuzao.common.pojo.main.PagePojo;
import com.jiwuzao.common.pojo.merchant.StorePagePojo;
import com.jiwuzao.common.pojo.order.GoodsReturnPojo;
import com.jiwuzao.common.vo.order.ReturnOrderVO;
import com.kauuze.major.config.permission.Authorization;
import com.kauuze.major.config.permission.Merchant;
import com.kauuze.major.service.OrderService;
import com.kauuze.major.service.ReturnService;
import com.qiniu.util.Json;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
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
    public JsonResult getReturnList(@RequestAttribute int uid, @Valid @RequestBody PagePojo pojo){
        PageDto<ReturnOrderVO> pageDto = returnService.getUserReturn(uid,pojo.getCurrentPage(),pojo.getPageSize());
        return JsonResult.success(pageDto);
    }

    @RequestMapping("/getReturnListByStore")
    @Merchant
    public JsonResult getReturnListByStore(@Valid @RequestBody StorePagePojo pojo){
        PageDto<ReturnOrderVO> pageDto = returnService.getReturnListByStore(pojo.getStoreId(),pojo.getCurrentPage(),pojo.getPageSize());
        return JsonResult.success();
    }
}
