package com.kauuze.major.api;

import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.jiwuzao.common.domain.enumType.GoodsReturnEnum;
import com.jiwuzao.common.domain.enumType.OrderStatusEnum;
import com.jiwuzao.common.domain.mongo.entity.userBastic.Store;
import com.jiwuzao.common.domain.mysql.entity.Receipt;
import com.jiwuzao.common.domain.mysql.entity.ReturnOrder;
import com.jiwuzao.common.dto.order.GoodsOrderDto;
import com.jiwuzao.common.dto.order.GoodsOrderSimpleDto;
import com.jiwuzao.common.include.JsonResult;
import com.jiwuzao.common.include.PageDto;
import com.jiwuzao.common.pojo.common.OrderStatusPojo;
import com.jiwuzao.common.pojo.order.*;
import com.jiwuzao.common.vo.order.GoodsOrderVO;
import com.jiwuzao.common.vo.order.ReceiptVO;
import com.jiwuzao.common.vo.store.StoreVO;
import com.kauuze.major.api.pojo.ConfirmRefundPojo;
import com.kauuze.major.config.permission.Authorization;
import com.kauuze.major.config.permission.Merchant;
import com.kauuze.major.service.AddressService;
import com.kauuze.major.service.MerchantService;
import com.kauuze.major.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    OrderService orderService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private MerchantService merchantService;

    /**
     * 获取沙箱支付key
     *
     * @return
     * @throws WxPayException
     */
    @RequestMapping("/getKey")
    public String getSandboxSignKey() throws WxPayException {
        String sandboxSignKey = wxPayService.getSandboxSignKey();
        System.out.println(sandboxSignKey);
        return sandboxSignKey;
    }

    /**
     * 用户传入收货信息、商品数组确认订单
     *
     * @param pojo 订单确认对象
     * @return
     */
    @RequestMapping("/confirmOrder")
    @Authorization
    public JsonResult confirmOrder(@RequestAttribute("uid") int uid, @RequestAttribute("ip") String ip,
                                   @Valid @RequestBody ConfirmOrderPojo pojo) throws WxPayException, IOException {
        Object result = orderService.confirmOrder(uid, pojo.getItemList(), pojo.getCity(),
                pojo.getAddress(), pojo.getPhone(), pojo.getTrueName(), ip, pojo.getReceipt(), pojo.getOpenId());
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
    public JsonResult getOrderSample(@RequestAttribute("uid") int uid, @RequestBody OrderStatusPojo pojo) {
        OrderStatusEnum status = null;
        if (pojo != null) {
            status = pojo.getStatus();
        }

        List<GoodsOrderSimpleDto> result = orderService.getOrderSample(uid, status);
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
        if (null == result) return JsonResult.failure();
        GoodsOrderVO goodsOrderVO = new GoodsOrderVO();
        BeanUtils.copyProperties(result, goodsOrderVO);
        StoreVO storeVO = merchantService.getStoreVO(goodsOrderVO.getSid());
        goodsOrderVO.setStoreIcon(storeVO.getStoreIcon()).setStoreName(storeVO.getStoreName());
        return JsonResult.success(goodsOrderVO);
    }

    /**
     * 获取订单详细信息
     */
    @RequestMapping("/getOrderReceipt")
    @Authorization
    public JsonResult getOrderReceipt(@RequestAttribute int uid, @Valid @RequestBody GetOrderPojo getOrderPojo) {
        Receipt receipt = orderService.getOrderReceipt(getOrderPojo.getGoodsOrderNo());
        if (receipt == null) {
            return JsonResult.success();
        } else {
            ReceiptVO receiptVO = new ReceiptVO();
            BeanUtils.copyProperties(receipt, receiptVO, "type");
            receiptVO.setType(receipt.getType().getMsg());
            return JsonResult.success(receiptVO);
        }
    }


    /**
     * 催单
     */
    @RequestMapping("/hastenOrder")
    @Authorization
    public JsonResult hastenOrder(@Valid @RequestBody GetOrderPojo getOrderPojo) {
        boolean result = orderService.hastenOrder(getOrderPojo.getGoodsOrderNo());
        if (!result) {
            return JsonResult.failure();
        } else {
            return JsonResult.success();
        }
    }

    /**
     * 取消订单
     */
    @RequestMapping("/cancelOrder")
    @Authorization
    public JsonResult cancelOrder(@RequestAttribute int uid, @Valid @RequestBody GetOrderPojo getOrderPojo) {
        String result = orderService.cancelOrder(uid, getOrderPojo.getGoodsOrderNo());
        if (result == null) {
            return JsonResult.failure();
        } else {
            return JsonResult.success(result);
        }
    }

    /**
     * 申请售后
     */
    @RequestMapping("/askService")
    @Authorization
    public JsonResult askService(@RequestAttribute int uid, @Valid @RequestBody AskServicePojo pojo) {
        String result = orderService.askService(uid, pojo.getGoodsOrderNo(), pojo.getContent());
        if (result == null) {
            return JsonResult.failure();
        } else {
            return JsonResult.success(result);
        }
    }

    @RequestMapping("/askGoodsReturn")
    @Authorization
    public JsonResult askGoodsReturn(@RequestAttribute int uid, @RequestBody GoodsReturnPojo pojo) {
        log.info("入参{}",pojo);
        ReturnOrder returnOrder = orderService.askGoodsReturn(uid, pojo.getGoodsOrderNo(), pojo.getReturnContent(), pojo.getImage());
        if (returnOrder != null) {
            return JsonResult.success(returnOrder);
        } else {
            return JsonResult.failure();
        }
    }

    /**
     * 商家确认退款
     */
    @RequestMapping("/confirmRefund")
    @Merchant
    public JsonResult confirmRefund(@RequestAttribute int uid, @Valid @RequestBody ConfirmRefundPojo pojo) throws WxPayException {
        WxPayRefundResult result = orderService.confirmRefund(uid, pojo.getGoodsOrderNo(), pojo.getAmount());
        if (result == null) {
            return JsonResult.failure();
        } else {
            return JsonResult.success(result);
        }
    }

    /**
     * 商家查看店铺所有下单情况
     *
     * @param uid  用户id
     * @param page 分页参数
     * @return pageDto 分页对象
     */
    @Merchant
    @RequestMapping("/getAllMerchantOrder")
    public JsonResult getAllMerchantOrder(@RequestAttribute int uid, @Valid @RequestBody OrderPagePojo page) {
        Store store = merchantService.getMerchantStore(uid);
        if (null == store) {
            return JsonResult.error("未找到店铺");
        }
        PageDto<GoodsOrderDto> pageDto = orderService.findAllOrderByStore(store.getId(), null, PageRequest.of(page.getCurrentPage(), page.getPageSize(), Sort.by(page.getIsAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, page.getSortBy())));
        return JsonResult.success(pageDto);
    }

    /**
     * 商家查看店铺按照订单状态查询
     *
     * @param uid  用户id
     * @param page 分页参数
     * @return pageDto 分页对象
     */
    @Merchant
    @RequestMapping("/getAllStoreOrderByStatus")
    public JsonResult getAllMerchantOrderByStatus(@RequestAttribute int uid, @Valid @RequestBody OrderPagePojo page) {
        Store store = merchantService.getMerchantStore(uid);
        if (null == store) {
            return JsonResult.error("未找到店铺");
        }
        PageDto<GoodsOrderDto> pageDto = orderService.findAllOrderByStore(store.getId(), page.getOrderStatus(), PageRequest.of(page.getCurrentPage(), page.getPageSize(), Sort.by(page.getIsAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, page.getSortBy())));
        return JsonResult.success(pageDto);
    }

}
