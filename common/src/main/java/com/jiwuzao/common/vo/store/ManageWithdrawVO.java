package com.jiwuzao.common.vo.store;

import com.jiwuzao.common.domain.enumType.OpeningBankEnum;
import com.jiwuzao.common.domain.enumType.WithdrawStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ManageWithdrawVO {

    private Integer withDrawId;
    private Integer uid;
    private String storeId;

    private String storeName;

    /**
     * 提现状态
     */
    @Enumerated(EnumType.STRING)
    private WithdrawStatusEnum withdrawStatus;
    /**
     * 提现单号
     */
    private String withdrawOrderNo;
    /**
     * 提现金额:1000以上可提
     */
    private BigDecimal remitMoney;
    /**
     * 银行卡号
     */
    private Long bankNo;
    /**
     * 银行卡姓名
     */
    private String bankTrueName;
    /**
     * 银行卡开户行
     */
    @Enumerated(EnumType.STRING)
    private OpeningBankEnum openingBank;
    /**
     * 提现备注
     */
    private String remark;
    /**
     * 处理时间:汇款前设置,若故障则查询最后一次操作
     */
    private Long processingTime;
    /**
     * 处理完成时间:汇款后设置
     */
    private Long finishTime;
    /**
     * 提现失败原因
     */
    private String failureReason;

    private Long createTime;
}

