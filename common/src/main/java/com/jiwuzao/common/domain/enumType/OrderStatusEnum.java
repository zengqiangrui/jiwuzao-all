package com.jiwuzao.common.domain.enumType;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-20 18:51
 */
public enum OrderStatusEnum {
    /**
     * 待支付
     */
    waitPay,
    /**
     * 待发货
     */
    waitDeliver,
    /**
     * 待收货(发货后15天自动收货)
     */
    waitReceive,
    /**
     * 待评价(收货后15天自动评价)
     */
    waitAppraise,
    /**
     * 已完成
     */
    finish,
    /**
     * 已取消
     */
    cancel
}