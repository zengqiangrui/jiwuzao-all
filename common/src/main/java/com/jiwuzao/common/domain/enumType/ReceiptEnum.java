package com.jiwuzao.common.domain.enumType;

import lombok.Getter;

@Getter
public enum ReceiptEnum {
    INDIVIDUAL(0,"个人"),
    COMPANY(1,"公司")
    ;

    private Integer code;

    private String msg;

    ReceiptEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
