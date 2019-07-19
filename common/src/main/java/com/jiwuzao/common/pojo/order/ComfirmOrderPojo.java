package com.jiwuzao.common.pojo.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ComfirmOrderPojo {
    /**
     * 订单id
     */
    private String goodsOrderNo;
    /**
     * 收货省市区
     */
    private String city;
    /**
     *收货详细地址
     */
    private String address;
    /**
     * 收货手机
     */
    private String phone;

    /**
     * 收货姓名
     */
    private String trueName;
}
