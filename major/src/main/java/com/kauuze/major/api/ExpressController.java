package com.kauuze.major.api;

import com.jiwuzao.common.domain.enumType.OrderStatusEnum;
import com.jiwuzao.common.domain.mongo.entity.userBastic.Store;
import com.jiwuzao.common.domain.mysql.entity.GoodsOrder;
import com.jiwuzao.common.dto.order.UserGoodsOrderDto;
import com.jiwuzao.common.exception.StoreException;
import com.jiwuzao.common.exception.excEnum.StoreExceptionEnum;
import com.jiwuzao.common.include.JsonResult;
import com.jiwuzao.common.pojo.order.ExpressPojo;
import com.kauuze.major.config.permission.Merchant;
import com.kauuze.major.service.ExpressService;
import com.kauuze.major.service.MerchantService;
import com.kauuze.major.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/express")
public class ExpressController {

    @Autowired
    private ExpressService expressService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MerchantService merchantService;

    @RequestMapping("/deliveryOne")
    @Merchant
    public JsonResult deliveryGoods(@RequestAttribute int uid, @RequestBody @Valid ExpressPojo express) {
        Store store = merchantService.getMerchantStore(uid);
        if (null == store) {
            throw new StoreException(StoreExceptionEnum.STORE_NOT_FOUND);//未找到店铺
        }
        if(store.getViolation()){
            throw new StoreException(StoreExceptionEnum.STORE_ILLEGAL);//店铺违规
        }
        GoodsOrder goodsOrder = expressService.addExpressOrder(express.getExpCode(), express.getExpNo(), express.getOrderNo());
        if(goodsOrder!=null){
            return JsonResult.success();
        }
        return JsonResult.failure();
    }

    /**
     * 商家查看店铺所有下单情况
     *
     * @param uid
     * @return
     */
    @Merchant
    @RequestMapping("/getAllDelivery")//todo 查找店铺所有的订单
    public JsonResult getAllDelivery(@RequestAttribute int uid) {

//        List<UserGoodsOrderDto> userOrder = orderService.getUserOrder(store.getId());
//        return JsonResult.success(userOrder);
        return null;
    }

    /**
     * 商家查看店铺未发货订单情况
     *
     * @param uid
     * @return
     */
    @Merchant
    @RequestMapping("/getAllWaitDeliver")
    public JsonResult getAllWaitDeliver(@RequestAttribute int uid) {
        Store store = merchantService.getMerchantStore(uid);
        if (null == store) {
            return JsonResult.failure("该商家没有店铺");
        }
        List<UserGoodsOrderDto> userOrder = orderService.getUserOrderByStatus(store.getId(), OrderStatusEnum.waitDeliver);
        return JsonResult.success(userOrder);
    }
}
