package com.kauuze.major.api;

import com.jiwuzao.common.domain.enumType.OrderStatusEnum;
import com.jiwuzao.common.domain.mongo.entity.ExpressResult;
import com.jiwuzao.common.domain.mongo.entity.userBastic.Store;
import com.jiwuzao.common.domain.mysql.entity.GoodsOrder;
import com.jiwuzao.common.dto.express.*;
import com.jiwuzao.common.dto.order.GoodsOrderDto;
import com.jiwuzao.common.dto.order.GoodsOrderSimpleDto;
import com.jiwuzao.common.dto.order.UserGoodsOrderDto;
import com.jiwuzao.common.exception.StoreException;
import com.jiwuzao.common.exception.excEnum.StoreExceptionEnum;
import com.jiwuzao.common.include.JsonResult;
import com.jiwuzao.common.include.JsonUtil;
import com.jiwuzao.common.include.PageDto;
import com.jiwuzao.common.include.StringUtil;
import com.jiwuzao.common.pojo.common.PagePojo;
import com.jiwuzao.common.pojo.order.ExpressPojo;
import com.jiwuzao.common.pojo.order.GetOrderPojo;
import com.jiwuzao.common.pojo.order.OrderPagePojo;
import com.kauuze.major.config.permission.Authorization;
import com.kauuze.major.config.permission.GreenWay;
import com.kauuze.major.config.permission.Merchant;
import com.kauuze.major.service.ExpressService;
import com.kauuze.major.service.MerchantService;
import com.kauuze.major.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.bouncycastle.asn1.eac.CertificateBody.requestType;


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
        checkStoreStatus(uid);
        GoodsOrder goodsOrder = expressService.addExpressOrder(express.getExpCode(), express.getExpNo(), express.getOrderNo());
        if (goodsOrder != null) {
            try {
                /**
                 * 订阅物流轨迹的推送
                 */
                ExpressRequestReturnDto returnDto = expressService.orderTracesSubByJson(express.getExpCode(), express.getExpNo(), express.getOrderNo(), express.getSenderAddressId());
                if (returnDto.getSuccess())
                    return JsonResult.success(returnDto);
                else
                    return JsonResult.failure(returnDto.getReason());
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    /**
     * 获取所有发货商品
     *
     * @param uid
     * @return
     */
    @Merchant
    @RequestMapping("/getAllWaitReceive")
    public JsonResult getAllWaitReceive(@RequestAttribute int uid) {
        Store store = checkStoreStatus(uid);
        List<UserGoodsOrderDto> userOrder = orderService.getUserOrderByStatus(store.getId(), OrderStatusEnum.waitReceive);
        return JsonResult.success(userOrder);
    }

    /**
     * 根据单一商品订单查询物流轨迹（本地数据查询）
     *
     * @param getOrderPojo
     * @return
     */
    @RequestMapping("/getTraceByOrder")
    @Authorization
    public JsonResult getTraceByOrder(@Valid @RequestBody GetOrderPojo getOrderPojo) {
        ExpressShowDto showDto = expressService.getExpressOneByOrder(getOrderPojo.getGoodsOrderNo());
        return JsonResult.success(showDto);
    }

    /**
     * 快递鸟一次查询获取订单信息
     *
     * @param expressPojo
     * @return
     */
    @RequestMapping("/getOrderTraceByKdniao")
    @Authorization
    public JsonResult getOrderTraceByKdniao(@Valid @RequestBody ExpressPojo expressPojo) {
        try {
            ExpressResult result = expressService.getOrderTracesByJson(expressPojo.getExpCode(), expressPojo.getExpNo(), expressPojo.getOrderNo());
            return JsonResult.success(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonResult.failure();
    }

    /**
     * 接收轨迹推送信息
     *
     * @param request
     * @return
     */
    @RequestMapping("/notify")
    public String getExpressNotify(HttpServletRequest request) throws RuntimeException {
        Map<String, String[]> parameterMap = request.getParameterMap();
        String requestData = parameterMap.get("RequestData")[0];//{"PushTime":"2019-07-25 16:32:16","EBusinessID":"test1554228","Data":[{"LogisticCode":"1234561","ShipperCode":"SF","Traces":[{"AcceptStation":"顺丰速运已收取快件","AcceptTime":"2019-07-25 16:32:16","Remark":""},{"AcceptStation":"货物已经到达深圳","AcceptTime":"2019-07-25 16:32:162","Remark":""},{"AcceptStation":"货物到达福田保税区网点","AcceptTime":"2019-07-25 16:32:163","Remark":""},{"AcceptStation":"货物已经被张三签收了","AcceptTime":"2019-07-25 16:32:164","Remark":""}],"State":"3","EBusinessID":"test1554228","Success":true,"Reason":"","CallBack":"","EstimatedDeliveryTime":"2019-07-25 16:32:16"}],"Count":"1"}
        String dataSign = parameterMap.get("DataSign")[0];//MjcwOGRmOTEyNTVjYjM5OWEwY2I1Yzc5MjRhODQxOGU=
        String requestType = parameterMap.get("RequestType")[0];//101
        ExpressNotifySendDto expressNotifySendDto = expressService.handleNotify(requestData, dataSign, requestType);
        log.info("expressDto",expressNotifySendDto);
        return JsonUtil.toJsonString(expressNotifySendDto);
    }

    /**
     * 用于验证店铺信息是否正确
     *
     * @param uid 用户id
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
