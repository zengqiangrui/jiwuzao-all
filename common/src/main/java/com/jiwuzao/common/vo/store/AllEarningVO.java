package com.jiwuzao.common.vo.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AllEarningVO {

    private String storeId;

    private BigDecimal turnover;//总营业额

    private BigDecimal allEarning;//总收益（提现后收益）

}
