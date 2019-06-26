package com.kauuze.order.domain.enumType;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-31 15:31
 */
public enum WithdrawStatusEnum {
    /**
     * 待处理，未向微信发请求
     */
    wait,
    /**
     * 处理中
     */
    processing,
    /**
     * 成功
     */
    success,
    /**
     * 失败
     */
    failure
}
