package com.jiwuzao.common.exception.excEnum;

import lombok.Getter;

@Getter
public enum OrderExceptionEnum {

    SPEC_NOT_FOUND(1, "规格信息没有找到"),
    ORDER_NOT_FOUND(2, "订单信息没有找到"),
    NOT_ENOUGH_STOCK(3, "库存不足"),
    NOT_SUPPORT_EXPRESS_CODE(4, "不支持的快递公司编码"),
    DEDUCTION_STOCK_ERROR(5, "扣库异常");

    private Integer code;

    private String msg;

    OrderExceptionEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
