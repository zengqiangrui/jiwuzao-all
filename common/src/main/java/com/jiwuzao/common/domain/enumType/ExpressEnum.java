package com.jiwuzao.common.domain.enumType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public enum  ExpressEnum {
    COMMON(0,"常用快递公司"),
    DOMESTIC(1,"国内快递公司"),
    ABROAD(2,"国外快递公司"),
    TRANS(3,"转运快递公司")
    ;
    private Integer code;
    private String msg;
    ExpressEnum(int code,String msg) {
        this.code = code;
        this.msg = msg;
    }
}
