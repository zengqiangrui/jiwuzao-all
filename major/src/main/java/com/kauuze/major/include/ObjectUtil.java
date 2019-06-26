package com.kauuze.major.include;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectUtil {

    /**
     * 获取传入对象的指定属性的值
     */
    public static Object getValueByPropName(Object obj, String propName) {
        Object value = null;
        try {
            // 获取对象的属性
            Field field = obj.getClass().getDeclaredField(propName);
            // 对象的属性的访问权限设置为可访问
            field.setAccessible(true);
            value = field.get(obj);
        } catch (Exception e) {
            return null;
        }

        return value;
    }
     
    /**
     * 根据传入对象获取其属性、类型、属性值的集合
     */
    public static List<Map<String, Object>> getFiledsInfo(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> infoMap = null;
        for (int i = 0; i < fields.length; i++) {
            infoMap = new HashMap<String, Object>();
            infoMap.put("type", fields[i].getType().toString());
            infoMap.put("name", fields[i].getName());
            infoMap.put("value", getValueByPropName(o, fields[i].getName()));
            list.add(infoMap);
        }
        return list;
    }
}