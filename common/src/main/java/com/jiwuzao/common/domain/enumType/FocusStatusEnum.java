package com.jiwuzao.common.domain.enumType;

import lombok.Getter;

@Getter
public enum FocusStatusEnum {
    NONE(1,"互不关注"),
    SINGLE(2,"单方向关注"),
    MUTUAL(3,"互相关注")
    ;
    private Integer code;
    private String msg;

    FocusStatusEnum(Integer code,String msg) {
        this.code= code;
        this.msg = msg;
    }
}
