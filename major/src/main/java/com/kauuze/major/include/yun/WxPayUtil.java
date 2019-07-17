package com.kauuze.major.include.yun;

import com.github.binarywang.wxpay.bean.request.WxPayOrderQueryRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.jiwuzao.common.config.contain.SpringContext;
import com.kauuze.major.include.DateTimeUtil;
import com.kauuze.major.include.JsonUtil;
import com.kauuze.major.include.StringUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-28 14:54
 */
public class WxPayUtil {
    /**
     * 生成二维码支付
     * @param productId
     * @param goodsName
     * @param payOrderNo
     * @param money
     * @param userIp
     * @return
     */
    public static String generateWxPayQrCode(String productId, String goodsName, String payOrderNo, BigDecimal money, String callBack, String userIp){
        WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
        request.setSpbillCreateIp(userIp);
        request.setTradeType("NATIVE");
        request.setProductId(productId);
        request.setBody("极物造-" + goodsName);
        request.setOutTradeNo(payOrderNo);
        request.setTimeExpire(DateTimeUtil.covertFormat(System.currentTimeMillis() + DateTimeUtil.getOneHourMill(),"yyyyMMddHHmmss"));
        request.setTotalFee(money.multiply(new BigDecimal("100")).intValue());
        request.setNotifyUrl(callBack);
        try {
            WxPayService wxPayService = SpringContext.getBean(WxPayService.class);
            Map map = JsonUtil.copy(wxPayService.createOrder(request),Map.class);
            return map.get("codeUrl").toString();
        } catch (WxPayException e) {
            e.printStackTrace();
            throw new RuntimeException("生成微信支付二维码失败");
        }
    }

    /**
     * 根据内部的订单号查微信订单:仅代表查到，具体处理应加锁去重
     * @param outTradeNo
     * @return
     */
    public static Map<String,String> queryOrder(String outTradeNo){
        WxPayOrderQueryRequest request = new WxPayOrderQueryRequest();
        request.setOutTradeNo(outTradeNo);
        try {
            WxPayService wxPayService = SpringContext.getBean(WxPayService.class);
            WxPayOrderQueryResult result = wxPayService.queryOrder(request);
            if(!StringUtil.isEq(result.getTradeState(),"SUCCESS")){
                return null;
            }
            Map<String,String> map = new HashMap<>();
            map.put("openId",result.getOpenid());
            map.put("transactionId",result.getTransactionId());
            return map;
        } catch (WxPayException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 微信打款
     * @return
     */
    public static boolean wxRemit( String wxRemitOpenId,String wxRemitTrueName,BigDecimal money){
        return true;
    }
}
