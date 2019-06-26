package com.kauuze.order.domain.enumType;

/**
 * 卖家退款类型
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-20 21:39
 */

public enum RefundTypeEnum {
    /**
     * 正常不退款(正常流程)
     */
    normal,
    /**
     * 暂停发货(退款协商中)
     */
    pauseDeliver,
    /**
     * 不发货退款(订单取消)
     */
    noDeliverRefundMoney,
    /**
     * 发货退款(正常流程)
     */
    deliverRefundMoney
}
