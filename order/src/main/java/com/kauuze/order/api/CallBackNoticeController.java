package com.kauuze.order.api;

import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.kauuze.order.domain.mongo.entity.Log;
import com.kauuze.order.domain.mongo.repository.LogRepository;
import com.kauuze.order.include.DateTimeUtil;
import com.kauuze.order.service.CallBackNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-28 13:30
 */
@RestController
public class CallBackNoticeController {
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private CallBackNoticeService callBackNoticeService;
    @Autowired
    private LogRepository logRepository;

    @RequestMapping("/systemGoodsWxNotice/wxQrCode")
    public String systemGoodsWxNotice(@RequestBody String xmlData){
        try {
            WxPayOrderNotifyResult notifyResult = wxPayService.parseOrderNotifyResult(xmlData);
            callBackNoticeService.systemGoodsWxNotice(notifyResult.getOutTradeNo());
        } catch (WxPayException e) {
            e.printStackTrace();
            logRepository.save(new Log(null,System.currentTimeMillis(),e.getMessage(),"order",true, DateTimeUtil.covertDateView(System.currentTimeMillis())));
        }
        return WxPayNotifyResponse.success("success");
    }
}
