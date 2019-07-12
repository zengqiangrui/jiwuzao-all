package com.kauuze.major.include;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        if ("".equals(str) || "null".equals(str)) {
            return true;
        }
        return false;
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

    @Test
    public void show() {
        System.out.println(getUrn("http://cdn.jiwuzao.com/8141c132a13a4a7cb8db0379c1ea4f82.mp4"));
    }
}
