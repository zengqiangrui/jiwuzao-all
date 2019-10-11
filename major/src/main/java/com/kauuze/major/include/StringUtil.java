package com.kauuze.major.include;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-07 00:19
 */
public class StringUtil {
    public static boolean isBlank(String str) {
        if (str == null) {
            return true;
        }
        str = str.trim();
        return "".equals(str) || "null".equals(str) || "undefined".equals(str);
    }

    public static boolean isEq(String value, String value2) {
        if (isBlank(value) || isBlank(value2)) {
            return false;
        }
        return value.equals(value2);
    }

    public static List<String> splitComma(String s) {
        if (s == null) {
            return new ArrayList<>();
        }
        String[] strs = s.split(",");
        List<String> list = Arrays.asList(strs);
        return list;
    }

    public static String joinComma(List<String> list) {
        if (list == null) {
            return null;
        }
        return String.join(",", list);
    }

    public static String hexBinDecOct(int n, int num) {
        String str = "";
        int yushu;
        int shang = num;
        while (shang > 0) {
            yushu = shang % n;
            shang = shang / n;
            if (yushu > 9) {
                str = (char) ('a' + (yushu - 10)) + str;
            } else {
                str = yushu + str;
            }
        }
        return str;
    }

    public static String getUrn(String uri) {
        return uri.substring(uri.lastIndexOf("/") + 1, uri.length());
    }

    public static String getRandomNickName(int len) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < len; i++) {
            String str = null;
            int hightPos, lowPos; // 定义高低位
            Random random = new Random();
            hightPos = (176 + Math.abs(random.nextInt(39))); // 获取高位值
            lowPos = (161 + Math.abs(random.nextInt(93))); // 获取低位值
            byte[] b = new byte[2];
            b[0] = (new Integer(hightPos).byteValue());
            b[1] = (new Integer(lowPos).byteValue());
            try {
                str = new String(b, "GBK"); // 转成中文
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            ret.append(str);
        }
        return ret.toString();
    }


}
