package com.jiwuzao.common.vo.store;

import com.jiwuzao.common.domain.enumType.OpeningBankEnum;
import com.jiwuzao.common.domain.enumType.WithdrawStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class WithdrawHistoryVO {
    private String withdrawOrderNo;

    private BigDecimal remitMoney;

    private OpeningBankEnum openingBank;

    private Long bankNo;

    private String bankTrueName;

    private String remark;

    private WithdrawStatusEnum withdrawStatus;

    private Long createTime;

    private Long processingTime;

    private Long finishTime;
}
