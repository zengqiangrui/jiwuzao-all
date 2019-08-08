package com.jiwuzao.common.domain.enumType;

import lombok.Getter;

@Getter
public enum DeliveryTimeEnum {
    ONE_DAY(1,"一天内"),
    TWO_DAY(2,"两天内"),
    TREE_DAY(3,"三天内"),
    FIVE_DAY(4,"五天内"),
    SEVEN_DAY(5,"七天内"),
    OTHERS(6,"其他时间");

    private Integer code;
    private String msg;

    DeliveryTimeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
