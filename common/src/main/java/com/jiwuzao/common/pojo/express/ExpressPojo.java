package com.jiwuzao.common.pojo.express;

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
    private String goodsOrderNo;//本订单号
    @StringMax(max = 32)
    private String expNo;//快递单号
    @StringMax(max = 10)
    private String expCode;//快递公司代码
}
