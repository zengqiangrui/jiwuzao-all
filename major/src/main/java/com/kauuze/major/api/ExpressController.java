package com.kauuze.major.api;

import com.jiwuzao.common.domain.enumType.OrderStatusEnum;
import com.jiwuzao.common.domain.mongo.entity.userBastic.Store;
import com.jiwuzao.common.domain.mysql.entity.GoodsOrder;
import com.jiwuzao.common.dto.order.GoodsOrderDto;
import com.jiwuzao.common.dto.order.GoodsOrderSimpleDto;
import com.jiwuzao.common.dto.order.UserGoodsOrderDto;
import com.jiwuzao.common.exception.StoreException;
import com.jiwuzao.common.exception.excEnum.StoreExceptionEnum;
import com.jiwuzao.common.include.JsonResult;
import com.jiwuzao.common.include.PageDto;
import com.jiwuzao.common.pojo.common.PagePojo;
import com.jiwuzao.common.pojo.order.ExpressPojo;
import com.jiwuzao.common.pojo.order.OrderPagePojo;
import com.kauuze.major.config.permission.GreenWay;
import com.kauuze.major.config.permission.Merchant;
import com.kauuze.major.service.ExpressService;
import com.kauuze.major.service.MerchantService;
import com.kauuze.major.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/express")
@Slf4j
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
        GoodsOrder goodsOrder = expressService.addExpressOrder(express.getExpCode(), express.getExpNo(), express.getOrderNo());
        if (goodsOrder != null) {
            return JsonResult.success();
        }
        return JsonResult.failure();
    }

    /**
     * 商家查看店铺所有下单情况
     *
     * @param uid  用户id
     * @param page 分页参数
     * @return pageDto 分页对象
     */
    @Merchant
    @RequestMapping("/getAllDelivery")
    public JsonResult getAllDelivery(@RequestAttribute int uid, @Valid @RequestBody OrderPagePojo page) {
        Store store = checkStoreStatus(uid);
        PageDto<GoodsOrderDto> pageDto = orderService.findAllOrderByStore(store.getId(), PageRequest.of(page.getCurrentPage(), page.getPageSize(), Sort.by(page.getIsAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, page.getSortBy())));
        return JsonResult.success(pageDto);
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
        Store store = checkStoreStatus(uid);
        List<UserGoodsOrderDto> userOrder = orderService.getUserOrderByStatus(store.getId(), OrderStatusEnum.waitDeliver);
        return JsonResult.success(userOrder);
    }

    @Merchant
    @RequestMapping("/getAllWaitReceive")
    public JsonResult getAllWaitReceive(@RequestAttribute int uid) {
        Store store = checkStoreStatus(uid);
        List<UserGoodsOrderDto> userOrder = orderService.getUserOrderByStatus(store.getId(), OrderStatusEnum.waitReceive);
        return JsonResult.success(userOrder);
    }

    @RequestMapping("/notify")
    @GreenWay
    public String getExpressNotify(@RequestParam(value = "RequestData") String requestData, @RequestParam(value = "requestType") String RequestType, @RequestParam(value = "DataSign") String dataSign) {
        log.info("RequestData", requestData);
        log.info("RequestType", RequestType);
        log.info("DataSign", dataSign);

        return "success";
    }


    /**
     * 用于验证店铺信息是否正确
     *
     * @param uid
     * @return
     */
    private Store checkStoreStatus(int uid) {
        Store store = merchantService.getMerchantStore(uid);
        if (null == store) {
            throw new StoreException(StoreExceptionEnum.STORE_NOT_FOUND);//未找到店铺
        }
        if (store.getViolation()) {
            throw new StoreException(StoreExceptionEnum.STORE_ILLEGAL);//店铺违规
        }
        return store;
    }
}
