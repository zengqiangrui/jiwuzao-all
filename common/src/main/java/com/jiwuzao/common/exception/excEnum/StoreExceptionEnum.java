package com.jiwuzao.common.exception.excEnum;

import lombok.Getter;

@Getter
public enum StoreExceptionEnum {
    VERIFY_NOT_AGREE(-1,"店铺未获取资质"),
    STORE_NOT_FOUND(1, "店铺信息没找到"),
    STORE_ILLEGAL(2,"店铺违规"),
    STORE_OVER_WITHDRAW(3,"店铺当日提现次数已达上限"),
    STORE_REMIT_SHORTAGE(4,"提现金额不足"),
    STORE_REMIT_EXCEED(5,"提现金额超过可提现金额"),
    STORE_NO_REMIT(6,"店铺未申请提现")
    ;
    private Integer code;

    private String msg;

    StoreExceptionEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
