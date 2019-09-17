package com.kauuze.major.api;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.jiwuzao.common.include.Rand;
import com.kauuze.major.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pay")
@Slf4j
public class PayController {

    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private PayService payService;

    /**
     * 根据支付方式调用统一下单接口
     * @param <T>
     * @return
     * @throws WxPayException
     */
    @RequestMapping("/createOrder")
    public <T> T createOrder() throws WxPayException {
        //必填项appid，mchid,随机字符串nonce_str,签名sign,已经根据配置给出
        /**
         * 以下为必填，其中ip从前端传入
         */
        WxPayUnifiedOrderRequest req = new WxPayUnifiedOrderRequest();
        req.setSpbillCreateIp("118.24.254.146");
        req.setNotifyUrl("http://api.jiwuzao.com/pay/notify/order");
        req.setTradeType(WxPayConstants.TradeType.APP);//支付类型,jsapi需要传openid
        req.setBody("testbody");//商品介绍
        req.setOutTradeNo(Rand.getUUID());//传给微信的订单号
        req.setTotalFee(1);//金额,分
        log.info("请求参数:{}",req);
        return this.wxPayService.createOrder(req);
    }

    /**
     *
     * @param xmlData
     * @return
     * @throws WxPayException
     */
    @PostMapping("/notify/order")
    public String parseOrderNotifyResult(@RequestBody String xmlData){
        /**
         * 解析回调的信息，完成进一步
         */
        final WxPayOrderNotifyResult notifyResult;
        try {
            notifyResult = wxPayService.parseOrderNotifyResult(xmlData);
            log.info("支付回调信息:{}",notifyResult);
            payService.handleNotify(notifyResult);
            return WxPayNotifyResponse.success("成功");
        } catch (WxPayException e) {
            e.printStackTrace();
            return WxPayNotifyResponse.fail("处理支付信息异常");
        }
    }

    /**
     *
     * @param xmlData
     * @return
     * @throws WxPayException
     */
    @PostMapping("/notify/refundOrder")
    public String parseRefundNotifyResult(@RequestBody String xmlData) throws WxPayException {
        /**
         * 解析回调的信息，完成进一步
         */
        final WxPayRefundNotifyResult notifyResult = wxPayService.parseRefundNotifyResult(xmlData);
        log.info("退款回调信息",notifyResult);
        payService.handleRefundNotify(notifyResult);
        return WxPayNotifyResponse.success("成功");
    }
}
