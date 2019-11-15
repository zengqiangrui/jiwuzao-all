package com.jiwuzao.common.domain.enumType;

import lombok.Getter;

@Getter
public enum DeliveryTimeEnum {
    ONE_DAY(1, "24时内", 1000 * 60 * 60L),
    TWO_DAY(2, "48时内", 2 * 1000 * 60 * 60L),
    TREE_DAY(3, "72时内", 3 * 1000 * 60 * 60L),
    FIVE_DAY(4, "五天内", 5 * 1000 * 60 * 60L),
    SEVEN_DAY(5, "七天内", 7 * 1000 * 60 * 60L),
    OTHERS(6, "其他时间", Long.MAX_VALUE);

    private Integer code;
    private String msg;
    private Long duration;

    DeliveryTimeEnum(int code, String msg, Long duration) {
        this.code = code;
        this.msg = msg;
        this.duration = duration;
    }
}
