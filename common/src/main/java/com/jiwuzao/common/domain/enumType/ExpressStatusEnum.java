package com.jiwuzao.common.domain.enumType;

import lombok.Getter;

@Getter
public enum ExpressStatusEnum {
    NO_RECORD("0", "没有记录"),
    EXPRESS_CONFIRM("1", "已揽收"),
    EXPRESS_ON_WAY("2", "运输途中"),
    ARRIVE_TARGET_CITY("201", "到达目的城市"),
    HAS_RECEIVED("3", "已签收"),
    PROBLEM_ITEM("4", "问题件");

    private String code;

    private String msg;

    ExpressStatusEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
