package com.kauuze.major.domain.enumType;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-27 20:26
 */
public enum SexEnum {
    /**
     * 男性
     */
    male("male"),
    /**
     * 女性
     */
    female("female"),
    /**
     * 未知
     */
    unknown("unknown");
    public String sex;

    SexEnum(String sex) {
        this.sex = sex;
    }
}
