package com.jiwuzao.common.pojo.order;

import com.jiwuzao.common.include.valid.StringMax;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ExpressPojo {
    @StringMax(max = 32)
    private String expCode;//快递公司编号
    @StringMax(max = 32)
    private String orderNo;//订单号
    @StringMax(max = 32)
    private String expNo;//快递物流号
    @StringMax(require = false,max = 200)
    private String orderNote;
    @StringMax(max = 32)
    private String addressId;//寄件人的地址id
}
