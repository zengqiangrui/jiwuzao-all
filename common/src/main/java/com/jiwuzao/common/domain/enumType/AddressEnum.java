package com.jiwuzao.common.domain.enumType;

import lombok.Getter;

@Getter
public enum  AddressEnum {
    USUAL(1,"普通地址"),
    DEFAULT(2,"默认地址"),
    DELETE(3,"用户已删除")
    ;

    AddressEnum(int value, String msg) {

    }
}
