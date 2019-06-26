package com.kauuze.manager.include;


import com.kauuze.manager.config.contain.SpringContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * http请求工具类
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-02-24 12:30
 */
public class HttpUtils {
    public static String post(String url, Map<String,String> query, Map<String, Object> body, HttpHeaders header) {
        if (query == null){
            query = new HashMap<>();
        }
        if(body == null){
            body = new HashMap<>();
        }
        if(header == null){
            header = new HttpHeaders();
        }
        RestTemplate restTemplate = SpringContext.getBean(RestTemplate.class);
        Map<String,List<String>> map = new HashMap<>();
        for (String s : query.keySet()) {
            List<String>  list = new ArrayList<>();
            list.add(query.get(s));
            map.put(s,list);
        }
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url).queryParams(new LinkedMultiValueMap<String,String>(map));
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(body, header);
        return restTemplate.postForObject(builder.build().encode().toUri(),httpEntity,String.class);
    }

    public static String get(String url,Map<String, String> query) {
        RestTemplate restTemplate = SpringContext.getBean(RestTemplate.class);
        Map<String,List<String>> map = new HashMap<>();
        for (String s : query.keySet()) {
            List<String>  list = new ArrayList<>();
            list.add(query.get(s));
            map.put(s,list);
        }
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url).queryParams(new LinkedMultiValueMap<String,String>(map));
        return restTemplate.getForObject(builder.build().encode().toUri(),String.class);
    }
}
