package com.kauuze.major.api;

import com.jiwuzao.common.dto.order.GoodsOrderDto;
import com.jiwuzao.common.dto.order.GoodsOrderSimpleDto;
import com.jiwuzao.common.include.JsonResult;
import com.jiwuzao.common.pojo.order.GenOrderPojo;
import com.kauuze.major.config.permission.Authorization;
import com.kauuze.major.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    OrderService orderService;

    /**
     * 用户通过购物车或者单个商品结算，传入商品数组和收货信息生成订单
     * @param uid
     * @param genOrderPojo
     * @return
     */
    @RequestMapping("/genOrder")
    @Authorization
    public JsonResult genOrder(@RequestAttribute int uid, @Valid @RequestBody GenOrderPojo genOrderPojo){
        String result = orderService.genOrder(uid, genOrderPojo.getItemList(),
                genOrderPojo.getCity(), genOrderPojo.getAddress(),
                genOrderPojo.getPhone(),genOrderPojo.getTrueName());
        if (result == null) {
            return JsonResult.failure();
        } else {
            return JsonResult.success(result);
        }
    }

    /**
     * 获取订单简略信息
     * @param uid
     * @return
     */
    @RequestMapping("/getOrderSample")
    @Authorization
    public JsonResult getOrderSample(@RequestAttribute int uid) {
        List<GoodsOrderSimpleDto> result = orderService.getOrderSample(uid);
        if (result == null) {
            return JsonResult.failure();
        } else {
            return JsonResult.success(result);
        }
    }

    /**
     * 获取订单详细信息
     */
    @RequestMapping("/getOrderDetail")
    @Authorization
    public JsonResult getOrderDetail(@RequestAttribute int uid) {
        GoodsOrderDto result = orderService.getOrderDetail(uid);
        if (result == null) {
            return JsonResult.failure();
        } else {
            return JsonResult.success(result);
        }
    }

}
