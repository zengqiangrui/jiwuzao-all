package com.kauuze.major.include.yun;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.kauuze.major.config.contain.properties.TencentSmsPropertis;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class TencentUtil {
    @Autowired
    private TencentSmsPropertis tencentSmsPropertis;

    /**
     * 发送验证码短信
     * @param phoneNumber
     * @param params
     * @return
     */
    public boolean sendSms(String phoneNumber, String[] params) {//params里面连个参数，第一个是验证码，第二个是时间
        try {
            SmsSingleSender ssender = new SmsSingleSender(tencentSmsPropertis.getAppId(), tencentSmsPropertis.getAppKey());
            SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNumber, tencentSmsPropertis.getSmsTemplateId(), params,"","","");  // 签名参数未提供或者为空时，会使用默认签名发送短信
            log.info("sendResult:{}",result);
            if(result.errMsg.equals("OK"))
                return true;
        } catch (HTTPException e) {
            // HTTP响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            // json解析错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络IO错误
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 发送发货通知短信
     * @param phoneNumber
     * @param params
     * @return
     */
    public boolean sendDeliverNotice(String phoneNumber, String[] params) {//params里面连个参数，第一个是商品名，第二个是规格
        try {
            SmsSingleSender sender = new SmsSingleSender(tencentSmsPropertis.getAppId(), tencentSmsPropertis.getAppKey());
            SmsSingleSenderResult result = sender.sendWithParam("86", phoneNumber, tencentSmsPropertis.getDeliverTemplateId(), params,"","","");  // 签名参数未提供或者为空时，会使用默认签名发送短信
            log.info("发货短信通知结果为：{}",result);
            if(result.errMsg.equals("OK"))
                return true;
        } catch (HTTPException e) {
            // HTTP响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            // json解析错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络IO错误
            e.printStackTrace();
        }
        return false;
    }

}
