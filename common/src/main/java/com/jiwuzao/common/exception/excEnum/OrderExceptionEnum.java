package com.jiwuzao.common.exception.excEnum;

import lombok.Getter;

@Getter
public enum OrderExceptionEnum {

    SPEC_NOT_FOUND(1, "规格信息没有找到"),
    ORDER_NOT_FOUND(2, "订单信息没有找到"),
    NOT_ENOUGH_STOCK(3, "库存不足"),
    NOT_SUPPORT_EXPRESS_CODE(4, "不支持的快递公司编码"),
    DEDUCTION_STOCK_ERROR(5, "扣库异常"),
    EXCEPTION_ORDER(6, "异常订单"),
    ORDER_DETAIL_NOT_FOUND(7, "订单信息没有找到"),
    ORDER_PAY_NOT_FIT(8, "支付金额不匹配"),
    REPEAT_PAY_ORDER(9, "重复支付回调"),

    CAN_NOT_REMIT(10, "不可提现"),
    NOT_PAID(11, "未支付"),
    DELIVER_FAIL(12, "发货失败"),
    HAS_RECEIPT(13, "已经开具发票"),
    ORDER_STATUS_NO_FIT(14, "订单状态不匹配"),
    REFUND_NOT_FOUND(15, "退款订单未找到"),
    GOODS_TAKE_OFF(16, "商品已下架"),
    USER_INVALID(17, "请求订单用户不匹配"),
    PROCESSING_RETURN(18, "处理退货中");

    private Integer code;

    private String msg;

    OrderExceptionEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
