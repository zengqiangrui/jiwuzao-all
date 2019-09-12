package com.jiwuzao.common.domain.enumType;

import lombok.Getter;

@Getter
public enum StoreStyleEnum {
    chinese("中国风"),
    japanese("日式"),
    ancient("复古"),
    fashion("潮派"),
    simple("简约");

    private String msg;

    StoreStyleEnum(String msg) {
        this.msg = msg;
    }
}