package com.kauuze.major.domain.mysql.entity;

import com.kauuze.major.domain.enumType.AuditTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

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
        @Index(name = "index_goodsOrder_trackingNo",columnList = "trackingNo",unique = true),
        @Index(name = "index_goodsOrder_applyCancel",columnList = "applyCancel"),
        @Index(name = "index_goodsOrder_applyCancelMerchantAudit",columnList = "trackingNo"),
        @Index(name = "index_goodsOrder_refundOrderNo",columnList = "refundOrderNo",unique = true),
        @Index(name = "index_goodsOrder_refund",columnList = "refund"),
        @Index(name = "index_goodsOrder_complaint",columnList = "complaint"),
        @Index(name = "index_goodsOrder_complaintAuditType",columnList = "complaintAuditType")
})
public class GoodsOrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 订单完成时间
     */
    private Long finishTime;
    /**
     * 订单取消时间
     */
    private Long cancelTime;

    /**
     * 物流单号
     */
    private String expressNo;
    /**
     * 收货省份
     */
    private String receiverProvince;
    /**
     * 收货城市
     */
    private String receiverCity;
    /**
     * 收货详细地址
     */
    private String receiverAddress;
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
    private boolean applyCancel = false;
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
     * 是否退款：由商家进行退款，取消订单全额退款。
     */
    private Boolean refund =false;
    /**
     * 退款时间
     */
    private Long refundTime;
    /**
     * 退款金额
     */
    private Boolean refundMoney;

    /**
     * 是否投诉
     */
    private Boolean complaint = false;
    /**
     * 投诉时间
     */
    private String complaintTime;
    /**
     * 投诉原因(富文本)
     */
    private String complaintReasons;
    /**
     * 投诉审批状态
     */
    @Enumerated(EnumType.STRING)
    private AuditTypeEnum complaintAuditType;
}
