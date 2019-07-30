package com.jiwuzao.common.pojo.userBasic;

import com.jiwuzao.common.domain.enumType.AddressEnum;
import com.jiwuzao.common.include.valid.Phone;
import com.jiwuzao.common.include.valid.StringMax;
import com.jiwuzao.common.include.valid.TrueName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AddressPojo {
    private String addressId;
    /**
     * 默认收货省,市,区
     */
    @StringMax
    private String receiveProvinces;
    /**
     *默认收货详细地址
     */
    @StringMax
    private String receiverAddress;

    /**
     * 默认收货手机
     */
    @Phone
    private String receiverPhone;

    /**
     * 默认收货姓名
     */
    @TrueName
    private String receiverTrueName;

    private AddressEnum addressStatus;

}
