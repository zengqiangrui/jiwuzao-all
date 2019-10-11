package com.jiwuzao.common.domain.enumType;

import lombok.Getter;

@Getter
public enum GoodsReturnEnum {
    CANNOT_RETURN(1,"不可退换"),
    CAN_RETURN(2,"七日可退换");

    private Integer code;

    private String msg;


    GoodsReturnEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
