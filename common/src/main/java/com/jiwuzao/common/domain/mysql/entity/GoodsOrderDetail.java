package com.jiwuzao.common.domain.mysql.entity;

import com.jiwuzao.common.domain.enumType.AuditTypeEnum;
import com.jiwuzao.common.exception.excEnum.OrderExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-06-04 13:54
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(indexes = {
        @Index(name = "index_goodsOrder_expressNo",columnList = "expressNo"),
        @Index(name = "index_goodsOrder_applyCancel",columnList = "applyCancel"),
        @Index(name = "index_goodsOrder_applyCancelMerchantAudit",columnList = "expressNo"),
        @Index(name = "index_goodsOrder_refundOrderNo",columnList = "refundOrderNo",unique = true),
        @Index(name = "index_goodsOrder_refund",columnList = "refund"),
        @Index(name = "index_goodsOrder_complaint",columnList = "complaint"),
        @Index(name = "index_goodsOrder_complaintAuditType",columnList = "complaintAuditType")
})
public class GoodsOrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String goodsOrderNo;

    /**
     * 订单完成时间
     */
    private Long finishTime;
    /**
     * 订单取消时间
     */
    private Long cancelTime;

    private String addressId;//寄件人的id（将会用于退货）

    /**
     * 物流单号
     */
    private String expressNo;
    /**
     * 快递公司编码
     */
    private String expCode;

    /**
     * 是否订阅成功
     */
    private Boolean isSubscribe;

    /**
     * 收货省市区
     */
    private String receiverCity;
    /**
     * 收货详细地址
     */
    private String receiverAddress;

    /**
     * 邮编
     */
    private String postCode;

    /**
     * 收货人
     */
    private String receiverTrueName;

    /**
     * 收货人手机
     */
    private String receiverPhone;
    /**
     * 发货前卖家对订单的备注
     */
    private String orderNote;
    /**
     * 是否申请取消订单:未审批无法发货
     */
    private boolean applyCancel;
    /**
     * 申请取消订单时间
     */
    private Long applyCancelTime;
    /**
     * 商家审批申请取消订单
     */
    @Enumerated(EnumType.STRING)
    private AuditTypeEnum applyCancelMerchantAudit;
    /**
     * 商家审批申请取消订单拒绝原因
     */
    private String applyCancelMerchantRefuseCause;

    /**
     * 退款单号
     */
    private String refundOrderNo;

    /**
     * 微信退款单号
     */
    private String weixinRefundId;
    /**
     * 是否退款：由商家进行退款，取消订单全额退款。
     */
    private Boolean refund;
    /**
     * 退款时间
     */
    private Long refundTime;
    /**
     * 退款金额
     */
    private BigDecimal refundMoney;

    /**
     * 是否投诉
     */
    private Boolean complaint;
    /**
     * 投诉时间
     */
    private Long complaintTime;
    /**
     * 投诉原因(富文本)
     */
    private String complaintReasons;
    /**
     * 投诉审批状态
     */
    @Enumerated(EnumType.STRING)
    private AuditTypeEnum complaintAuditType;
    /**
     * 记录异常订单原因
     */
    @Enumerated(EnumType.STRING)
    private OrderExceptionEnum exceptionReason;//订单异常原因


}
