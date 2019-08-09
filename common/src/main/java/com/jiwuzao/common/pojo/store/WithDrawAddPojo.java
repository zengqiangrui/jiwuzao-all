package com.jiwuzao.common.pojo.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class WithDrawAddPojo {

    private String orderId;

    private BigDecimal remitMoney;


}
