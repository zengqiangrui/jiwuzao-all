package com.kauuze.major.api;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.jiwuzao.common.include.Rand;
import com.kauuze.major.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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
        req.setNotifyUrl("http://api.jiwuzao.com/pay/notify/order");//todo 解析api.jiwuzao.com，同时本项目部署在此之上
        req.setTradeType(WxPayConstants.TradeType.APP);//支付类型,jsapi需要传openid
        req.setBody("testbody");//商品介绍
        req.setOutTradeNo(Rand.getUUID());//传给微信的订单号
        req.setTotalFee(1);//金额,分
        log.info("请求参数",req);
        if(this.wxPayService.createOrder(req) instanceof WxPayAppOrderResult){
            //todo
        }
        return this.wxPayService.createOrder(req);
    }

    /**
     *
     * @param xmlData
     * @return
     * @throws WxPayException
     */
    @PostMapping("/notify/order")
    public String parseOrderNotifyResult(@RequestBody String xmlData) throws WxPayException {
        /**
         * 解析回调的信息，完成进一步
         */
        final WxPayOrderNotifyResult notifyResult = wxPayService.parseOrderNotifyResult(xmlData);
        log.info("回调信息",notifyResult);
        payService.handleNotify(notifyResult);
        // TODO 根据自己业务场景需要构造返回对象
        return WxPayNotifyResponse.success("成功");
    }
}
