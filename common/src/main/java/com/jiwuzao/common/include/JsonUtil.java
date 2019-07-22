package com.jiwuzao.common.include;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * 处理json数据工具
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-03-05 20:34
 */
public class JsonUtil {
    /**
     * 将对象转为json格式
     * @param source
     * @return
     */
    public static String toJsonString(Object source){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            String jsonString = objectMapper.writeValueAsString(source);
            return jsonString;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * 将对象转为json格式，忽略空值
     * @param source
     * @return
     */
    public static String toJsonStringNonNull(Object source){
        ObjectMapper objectMapper  = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            String jsonString = objectMapper.writeValueAsString(source);
            return jsonString;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

    /**
     * 解析json字符串,忽略未知属性
     * @param jsonString
     * @param targetType
     * @param <T>
     * @return
     */
    public static <T> T parseJsonString(String jsonString,Class<T> targetType){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return objectMapper.readValue(jsonString,targetType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

    /**
     * 深拷贝,自动缺省字段
     * @param o
     * @param targetType
     * @param <T>
     * @return
     */
    public static <T> T copy(Object o,Class<T> targetType){
        return parseJsonString(toJsonString(o),targetType);
    }

    public static <T> T convertClass(Class<T> type){
        try {
            return type.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }
}
