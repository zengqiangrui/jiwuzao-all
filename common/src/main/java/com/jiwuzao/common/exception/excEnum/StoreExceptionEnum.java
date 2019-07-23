package com.jiwuzao.common.exception.excEnum;

import lombok.Getter;

@Getter
public enum StoreExceptionEnum {

    STORE_NOT_FOUND(1, "店铺信息没找到"),
    STORE_ILLEGAL(2,"店铺违规");
    private Integer code;

    private String msg;

    StoreExceptionEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
