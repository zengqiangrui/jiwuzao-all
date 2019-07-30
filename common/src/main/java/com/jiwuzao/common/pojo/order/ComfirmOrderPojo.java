package com.jiwuzao.common.pojo.order;

import com.jiwuzao.common.pojo.shopcart.AddItemPojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ComfirmOrderPojo {
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
    /**
     * 商品列表
     */
    private List<AddItemPojo> itemList;
}
