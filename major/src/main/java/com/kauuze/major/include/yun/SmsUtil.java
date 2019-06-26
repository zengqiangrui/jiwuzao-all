package com.kauuze.major.include.yun;


import com.kauuze.major.ConfigUtil;
import com.kauuze.major.include.HttpUtils;
import com.kauuze.major.include.JsonUtil;
import com.kauuze.major.include.StringUtil;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 发送短信验证码工具类
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-02-24 12:30
 */
public class SmsUtil {
    private static final String url = "http://yzx.market.alicloudapi.com/yzx/sendSms";
    private static final String appcode = "fa34001600344f58bbb9da93a3be1384";
    private static final String tp1 = "TP18032912";
    private static final HttpHeaders headers = new HttpHeaders();
    static {
        List<String> list = new ArrayList<>();
        list.add("APPCODE " + appcode);
        headers.put("Authorization", list);
    }
    /**
     *
     * @param phone 发送给手机号
     * @param code 发送的验证码
     * @return  false发送失败,true发送成功
     */
    public static boolean sendTp1(String phone,int code){
        if(StringUtil.isEq(ConfigUtil.customEnvironment,"dev")){
            System.out.println("(dev环境)手机号 " + phone + " 的短信验证码: " + code);
            return true;
        }
        Map<String,String> query = new HashMap<>();
        query.put("mobile", phone);
        query.put("param","code:" + code);
        query.put("tpl_id", tp1);
        return doSend(query,null);
    }


    private static boolean doSend(Map<String,String> query,Map<String,Object> body){
        String response = HttpUtils.post(url,query,body,headers);
        Map<String,String> map = JsonUtil.parseJsonString(response,Map.class);
        String returnCode = map.get("return_code");
        if(StringUtil.isEq(returnCode,"00000")){
            return true;
        }else {
            return false;
        }
    }


}

