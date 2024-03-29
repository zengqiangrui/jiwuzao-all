package com.jiwuzao.common.pojo.store;

import com.jiwuzao.common.include.valid.*;
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

    @StringMax
    private String storeId;

    @StringMax
    private String pwd;

    @StringMax(require = false)
    private String remark;

    @Decimal
    private BigDecimal remitMoney;

}
