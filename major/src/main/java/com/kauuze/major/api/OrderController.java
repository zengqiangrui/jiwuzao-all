package com.kauuze.major.api;

import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.jiwuzao.common.domain.enumType.OrderStatusEnum;
import com.jiwuzao.common.domain.mongo.entity.userBastic.Store;
import com.jiwuzao.common.dto.order.GoodsOrderDto;
import com.jiwuzao.common.dto.order.GoodsOrderSimpleDto;
import com.jiwuzao.common.include.JsonResult;
import com.jiwuzao.common.include.PageDto;
import com.jiwuzao.common.pojo.common.OrderStatusPojo;
import com.jiwuzao.common.pojo.order.AskServicePojo;
import com.jiwuzao.common.pojo.order.ComfirmOrderPojo;
import com.jiwuzao.common.pojo.order.GetOrderPojo;
import com.jiwuzao.common.pojo.order.OrderPagePojo;
import com.kauuze.major.config.permission.Authorization;
import com.kauuze.major.config.permission.Merchant;
import com.kauuze.major.service.AddressService;
import com.kauuze.major.service.MerchantService;
import com.kauuze.major.service.OrderService;
import lombok.extern.slf4j.Slf4j;
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


//    /**
//     * 用户通过购物车或者单个商品结算，传入商品数组生成订单
//     *
//     * @param uid
//     * @param genOrderPojo
//     * @return 返回订单id
//     */
//    @RequestMapping("/genOrder")
//    @Authorization
//    public JsonResult genOrder(@RequestAttribute int uid, @Valid @RequestBody GenOrderPojo genOrderPojo) {
//        String result = orderService.genOrder(uid, genOrderPojo.getItemList());
//        if (result == null) {
//            return JsonResult.failure();
//        } else {
//            return JsonResult.success(result);
//        }
//    }

    /**
     * 用户传入收货信息、商品数组确认订单
     *
     * @param pojo
     * @return
     */
    @RequestMapping("/comfirmOrder")
    @Authorization
    public JsonResult comfirmOrder(@RequestAttribute("uid") int uid, @RequestAttribute("ip") String ip,
                                   @Valid @RequestBody ComfirmOrderPojo pojo, HttpServletResponse response) throws WxPayException, IOException {
        Object result = orderService.comfirmOrder(uid, pojo.getItemList(), pojo.getCity(),
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
        if (result == null) {
            return JsonResult.failure();
        } else {
            return JsonResult.success(result);
        }
    }

    /**
     * 催单
     */
    @RequestMapping("/hastenOrder")
    @Authorization
    public JsonResult hastenOrder(@RequestAttribute int uid, @Valid @RequestBody GetOrderPojo getOrderPojo) {
        String result = orderService.hastenOrder(uid, getOrderPojo.getGoodsOrderNo());
        if (result == null) {
            return JsonResult.failure();
        } else {
            return JsonResult.success(result);
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
        PageDto<GoodsOrderDto> pageDto = orderService.findAllOrderByStore(store.getId(),null, PageRequest.of(page.getCurrentPage(), page.getPageSize(), Sort.by(page.getIsAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, page.getSortBy())));
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
        PageDto<GoodsOrderDto> pageDto = orderService.findAllOrderByStore(store.getId(), page.getOrderStatus(),PageRequest.of(page.getCurrentPage(), page.getPageSize(), Sort.by(page.getIsAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, page.getSortBy())));
        return JsonResult.success(pageDto);
    }

}
