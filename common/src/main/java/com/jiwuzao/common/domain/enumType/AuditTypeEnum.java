package com.jiwuzao.common.domain.enumType;

import lombok.Getter;

/**
 * 审批类型
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-25 16:22
 */
@Getter
public enum AuditTypeEnum {
    /**
     * 待审批
     */
    wait(1,"待审批"),
    /**
     * 同意
     */
    agree(2,"已同意"),
    /**
     * 拒绝
     */
    refuse(3,"已拒绝");

    private Integer code;
    private String msg;

    AuditTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
