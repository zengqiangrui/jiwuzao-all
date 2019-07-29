package com.jiwuzao.common.domain.enumType;

import lombok.Getter;

@Getter
public enum  AddressEnum {
    USUAL(1,"普通地址"),
    DEFAULT(2,"默认地址"),
    DELETE(3,"用户已删除")
    ;

    private Integer value;

    private String msg;

    AddressEnum(int value, String msg) {
        this.value = value;
        this.msg = msg;
    }
}
