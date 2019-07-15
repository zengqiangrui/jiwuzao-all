package com.jiwuzao.common.include;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 处理时间
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-22 11:57
 */
public class DateTimeUtil {

    /**
     * 转换为8位日期
     * @return
     */
    public static String covertDateNum(long mill){
        LocalDateTime localDateTime = covertLocalDateTime(mill);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return localDateTime.format(formatter);
    }

    /**
     * 转换可视日期格式
     * @param mill
     * @return
     */
    public static String covertDateView(Long mill){
        LocalDateTime localDateTime = covertLocalDateTime(Long.valueOf(String.valueOf(mill)));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }

    /**
     * 转换成任意日期格式
     * @param mill
     * @param format
     * @return
     */
    public static String covertFormat(Long mill,String format){
        LocalDateTime localDateTime = covertLocalDateTime(Long.valueOf(String.valueOf(mill)));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return localDateTime.format(formatter);
    }

    /**
     * 日期转毫秒
     * @param localDateTime
     * @return
     */
    public static Long covertMill(LocalDateTime localDateTime){
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 毫秒转日期
     * @param mill
     * @return
     */
    public static LocalDateTime covertLocalDateTime(long mill){
        return LocalDateTime.ofEpochSecond(mill/1000, 0, ZoneOffset.ofHours(8));
    }

    /**
     *前一个比后一个小多少
     * @return
     */
    public static Duration duration(long mill,long mill2){
       LocalDateTime localDateTime = covertLocalDateTime(mill);
       LocalDateTime localDateTime2 = covertLocalDateTime(mill2);
       return Duration.between(localDateTime,localDateTime2);
    }

    /**
     * 一年毫秒值
     * @return
     */
    public static Integer getOneYearMill(){
        return 365*getOneDayMill();
    }
    /**
     * 一个月毫秒值
     * @return
     */
    public static Integer getOneMonthMill(){
        return 30*getOneMonthMill();
    }
    /**
     * 一天毫秒值
     * @return
     */
    public static Integer getOneDayMill(){
        return 1000*60*60*24;
    }
    /**
     * 一小时毫秒值
     * @return
     */
    public static Integer getOneHourMill(){
        return 1000*60*60;
    }
    /**
     * 一分钟毫秒值
     * @return
     */
    public static Integer getOneMinMill(){
        return 1000*60;
    }

    public static List<Long> getYearStartAndEndMill(int year){
        LocalDateTime localDateTime = LocalDateTime.of(year,1,1,0,0);
        Long start = covertMill(localDateTime);
        Long end = covertMill(localDateTime.plusYears(1)) - 1;
        List<Long> list = new ArrayList<>();
        list.add(start);
        list.add(end);
        return list;
    }

    public static List<Long> getMonthStartAndEndMill(int year,int month){
        LocalDateTime localDateTime = LocalDateTime.of(year,month,1,0,0);
        Long start = covertMill(localDateTime);
        Long end = covertMill(localDateTime.plusMonths(1)) - 1;
        List<Long> list = new ArrayList<>();
        list.add(start);
        list.add(end);
        return list;
    }

    public static List<Long> getDayStartAndEndMill(int year,int month,int day){
        LocalDateTime localDateTime = LocalDateTime.of(year,month,day,0,0);
        Long start = covertMill(localDateTime);
        Long end = covertMill(localDateTime.plusDays(1)) - 1;
        List<Long> list = new ArrayList<>();
        list.add(start);
        list.add(end);
        return list;
    }
}
