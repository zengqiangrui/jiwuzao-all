package com.jiwuzao.common.domain.enumType;

import lombok.Getter;

@Getter
public enum OnlineStatusEnum {
    ON_LINE(1,"在线"),
    OFF_LINE(2,"不在线"),
    INVISIBLE(3,"隐身")
    ;

    private Integer code;

    private String msg;

    OnlineStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
