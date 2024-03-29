package com.jiwuzao.common.pojo.address;

import com.jiwuzao.common.domain.enumType.AddressEnum;
import com.jiwuzao.common.include.valid.Phone;
import com.jiwuzao.common.include.valid.StringMax;
import com.jiwuzao.common.include.valid.TrueName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.core.annotation.AliasFor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AddressPojo {
    private String addressId;
    /**
     * 默认收货省-市-区
     */
    @StringMax
    private String provinces;
    /**
     *默认收货详细地址
     */
    @StringMax
    private String addressDetail;

    /**
     * 默认收货手机
     */
    @Phone
    private String phone;

    /**
     * 默认收货姓名
     */
    @TrueName
    private String trueName;

    private AddressEnum addressStatus;

}
