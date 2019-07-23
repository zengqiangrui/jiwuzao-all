package com.kauuze.major.api;

import com.github.binarywang.wxpay.exception.WxPayException;
import com.jiwuzao.common.dto.order.GoodsOrderDto;
import com.jiwuzao.common.dto.order.GoodsOrderSimpleDto;
import com.jiwuzao.common.include.JsonResult;
import com.jiwuzao.common.pojo.order.ComfirmOrderPojo;
import com.jiwuzao.common.pojo.order.GenOrderPojo;
import com.jiwuzao.common.pojo.order.GetOrderPojo;
import com.kauuze.major.config.permission.Authorization;
import com.kauuze.major.service.AddressService;
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
    @Autowired
    private AddressService addressService;

    /**
     * 用户通过购物车或者单个商品结算，传入商品数组生成订单
     *
     * @param uid
     * @param genOrderPojo
     * @return 返回订单id
     */
    @RequestMapping("/genOrder")
    @Authorization
    public JsonResult genOrder(@RequestAttribute int uid, @Valid @RequestBody GenOrderPojo genOrderPojo) {
        String result = orderService.genOrder(uid, genOrderPojo.getItemList());
        if (result == null) {
            return JsonResult.failure();
        } else {
            return JsonResult.success(result);
        }
    }

    /**
     * 用户传入收货信息确认订单
     *
     * @param pojo
     * @return
     */
    @RequestMapping("/comfirmOrder")
    @Authorization
    public JsonResult comfirmOrder(@RequestAttribute("ip") String ip,
                                   @Valid @RequestBody ComfirmOrderPojo pojo) throws WxPayException {
        Object result = orderService.comfirmOrder(pojo.getGoodsOrderNo(), pojo.getCity(),
                pojo.getAddress(), pojo.getPhone(), pojo.getTrueName(), ip);
        if (result == null) {
            return JsonResult.failure();
        } else {
            return JsonResult.success(result);
        }
    }

    /**
     * 获取订单简略信息
     *
     * @param uid
     * @return
     */
    @RequestMapping("/getOrderSample")
    @Authorization
    public JsonResult getOrderSample(@RequestAttribute("uid") int uid) {
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
    public JsonResult getOrderDetail(@RequestAttribute int uid, @Valid @RequestBody GetOrderPojo getOrderPojo) {
        GoodsOrderDto result = orderService.getOrderDetail(getOrderPojo.getGoodsOrderNo());
        if (result == null) {
            return JsonResult.failure();
        } else {
            return JsonResult.success(result);
        }
    }

}
