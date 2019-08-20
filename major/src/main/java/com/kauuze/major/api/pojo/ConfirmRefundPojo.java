package com.kauuze.major.api.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ConfirmRefundPojo {
    /**
     * 商品订单号
     */
    private String goodsOrderNo;

    /**
     * 确认退款金额
     */
    private BigDecimal amount;

}
