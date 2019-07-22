package com.kauuze.major.api;

import com.jiwuzao.common.dto.order.GoodsOrderDto;
import com.jiwuzao.common.pojo.order.ExpressPojo;
import com.kauuze.major.config.permission.Merchant;
import com.kauuze.major.include.JsonResult;
import com.kauuze.major.service.ExpressService;
import com.kauuze.major.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/express")
public class ExpressController {

    @Autowired
    private ExpressService expressService;
    @Autowired
    private OrderService orderService;

    public JsonResult deliveryGoods(@RequestAttribute int uid, @RequestBody @Valid ExpressPojo express){
//        GoodsOrderDto orderDetail = orderService.getOrderDetail(orderId);
//        expressService.getOrderTracesByJson()
        return JsonResult.failure();
    }

    @Merchant
    @RequestMapping("/getAllDelivery")
    public JsonResult getAllDelivery(@RequestAttribute int uid){
        return null;
    }
}
