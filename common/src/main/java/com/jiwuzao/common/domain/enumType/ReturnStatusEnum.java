package com.jiwuzao.common.domain.enumType;

import lombok.Getter;

@Getter
public enum ReturnStatusEnum {

    WAIT_CONFIRM(1, "待确认"),//提交退货申请后商家待确认
    WAIT_DELIVER(2, "待发货"),
    WAIT_RECEIVE(3, "待收货"),//商家待收货
    WAIT_REFUND_CONFIRM(4, "等待退款确认"),
    PROCESSING_REFUND(5, "处理退款中"),//收货后确认退款
    SUCCESS(6, "成功"),
    FAIL(7, "失败"),
    CANCEL(8, "已取消");
    private Integer code;

    private String msg;

    ReturnStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
