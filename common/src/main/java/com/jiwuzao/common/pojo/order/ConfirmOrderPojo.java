package com.jiwuzao.common.pojo.order;

import com.jiwuzao.common.domain.enumType.ReceiptEnum;
import com.jiwuzao.common.include.valid.Phone;
import com.jiwuzao.common.include.valid.StringMax;
import com.jiwuzao.common.include.valid.TrueName;
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
public class ConfirmOrderPojo {
    /**
     * 收货省市区
     */
    @StringMax
    private String city;
    /**
     *收货详细地址
     */
    @StringMax
    private String address;
    /**
     * 收货手机
     */
    @Phone
    private String phone;

    /**
     * 收货姓名
     */
    @TrueName
    private String trueName;

    private ReceiptPojo receipt;

    /**
     * 商品列表
     */
    private List<AddItemPojo> itemList;
}
