package com.jiwuzao.common.domain.enumType;

import lombok.Getter;

@Getter
public enum ReturnStatusEnum {

    WAIT_CONFIRM(1,"待确认"),//提交退货申请后商家待确认
    WAIT_RECEIVE(2,"待收货"),//商家待收货
    SUCCESS(3,"成功"),
    FAIL(4,"失败")
    ;

    private Integer code;

    private String msg;

    ReturnStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
