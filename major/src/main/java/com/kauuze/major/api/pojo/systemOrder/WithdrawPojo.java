package com.kauuze.major.api.pojo.systemOrder;

import com.kauuze.major.include.valid.Decimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-06-06 16:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class WithdrawPojo {
    @Decimal
    private BigDecimal money;
}
