package com.jiwuzao.common.domain.mysql.entity;

import com.jiwuzao.common.domain.enumType.OpeningBankEnum;
import com.jiwuzao.common.domain.enumType.WithdrawStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-27 19:17
 */

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(indexes = {
        @Index(name = "index_withdrawWxOrder_uid",columnList = "uid"),
        @Index(name = "index_withdrawWxOrder_createTime",columnList = "createTime"),
        @Index(name = "index_withdrawWxOrder_withdrawStatus",columnList = "withdrawStatus"),
        @Index(name = "index_withdrawWxOrder_withdrawOrderNo",columnList = "withdrawOrderNo"),
        @Index(name = "index_withdrawWxOrder_remitMoney",columnList = "remitMoney")

})
public class WithdrawOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer uid;
    private String storeId;//店铺id
    private Long createTime;

    /**
     * 是否是保证金
     */
    private Boolean deposit = false;
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
    private String bankNo;
    /**
     * 银行卡姓名
     */
    private String bankTrueName;
    /**
     * 银行卡开户行
     */
    private String openingBank;
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
}
