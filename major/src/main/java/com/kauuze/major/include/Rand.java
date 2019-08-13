package com.kauuze.major.include;


import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * 随机数和毫秒值工具类
 *
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-02-24 12:30
 */
public class Rand {
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private static final Random RANDOM = new Random(System.currentTimeMillis());

    /**
     * @param count 获取几个数字
     * @return
     */
    public static int getNumber(int count) {
        int num = 1;
        for (int i = 0; i < count; i++) {
            num = num * 10;
        }
        num = num / 10 + RANDOM.nextInt(num - num / 10);
        return num;
    }

    /**
     * 获取[0,max)的数
     *
     * @param max
     * @return
     */
    public static int getRand(int max) {
        return RANDOM.nextInt(max);
    }

    public static boolean getOnOff() {
        int result = getRand(2);
        if (result == 0) {
            return true;
        }
        return false;
    }

    /**
     * @param count 获取几个字符
     * @return
     */
    public static String getString(int count) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (getOnOff()) {
                stringBuilder.append(getLetter());
            } else {
                stringBuilder.append(getRand(10));
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 获取[0,count)个字符
     *
     * @param count
     * @return
     */
    public static String getStringMax(int count) {
        return getString(getRand(count));
    }

    public static char getLetter() {
        int num = RANDOM.nextInt(26);
        char[] chars = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l'
                , 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        return chars[num];
    }

    /**
     * 生成订单号，策略为年月日时分秒拼接五位随机整数
     * @return String
     */
    public synchronized static String createOrderNo() {
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String str = simpleDateFormat.format(date);
        int ran = (int) (RANDOM.nextDouble() * (99999 - 10000 + 1)) + 10000;// 获取5位随机数
        return str + ran;
    }

}
