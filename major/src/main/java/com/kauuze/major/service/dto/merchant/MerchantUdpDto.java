package com.kauuze.major.service.dto.merchant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-06-09 19:06
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class MerchantUdpDto {
    /**
     * 保证金
     */
    private BigDecimal deposit;
    /**
     * 可提现金额
     */
    private BigDecimal withdrawal;
    /**
     * 正在提现金额
     */
    private BigDecimal onWithdrawalOrder;
    /**
     * 今日是否提现：一天最多1次,每次1000以上
     */
    private Boolean todayWithdrawal;

}
