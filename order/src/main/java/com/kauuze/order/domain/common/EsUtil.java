package com.kauuze.order.domain.common;


import com.kauuze.order.ConfigUtil;
import com.kauuze.order.include.HttpUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

/**
 * elasticsearach 搜索 增加数值
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-21 19:37
 */
public class EsUtil {

    /**
     * 增加某个字段数值
     */
    public static void inc(String gid,String field,Integer incNum){
        Map<String,Object> body = new HashMap<>();
        field = "ctx._source." + field;
        Map<String,String> inline = new HashMap<>();
        inline.put("inline",field + " = " + field + " + " + incNum);
        body.put("script",inline);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpUtils.post("http://" + ConfigUtil.elasticsearchNode + "/search_goods_" + ConfigUtil.customEnvironment + "/_doc/" + gid + "/_update",null,body,httpHeaders);
    }

    /**
     * 修改某个字段值
     * @param gid
     * @param map
     */
    public static void modify(String gid,Map<String,String> map){
        Map<String,Object> body = new HashMap<>();
        StringBuilder inline = new StringBuilder("");
        for (String s : map.keySet()) {
            inline = inline.append("ctx._source." + s  + " = '" + map.get(s) + "';");
        }
        Map<String,String> script = new HashMap<>();
        script.put("inline",inline.toString());
        body.put("script",script);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpUtils.post("http://" + ConfigUtil.elasticsearchNode + "/search_goods_" + ConfigUtil.customEnvironment + "/_doc/" + gid + "/_update",null,body,httpHeaders);
    }
}
