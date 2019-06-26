package com.kauuze.major.domain.common;


import com.kauuze.major.include.DateTimeUtil;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-23 11:44
 */
public class OrderUtil {
    /**
     * 商品订单号，支付订单号，提现订单号，退款订单号
     * @return
     */
    public static String getOrderNo(Integer orderId,String gpwrType){
        return  gpwrType + DateTimeUtil.covertDateNum(System.currentTimeMillis()) + orderId;
    }
}
