package com.kauuze.order.domain.mysql.entity;


import com.kauuze.order.domain.enumType.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-20 15:10
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(indexes = {
        @Index(name = "index_goodsOrder_uid",columnList = "uid"),
        @Index(name = "index_goodsOrder_sid",columnList = "sid"),
        @Index(name = "index_goodsOrder_gid",columnList = "gid"),
        @Index(name = "index_goodsOrder_pid",columnList = "pid"),
        @Index(name = "index_goodsOrder_createTime",columnList = "createTime"),
        @Index(name = "index_goodsOrder_finishTime",columnList = "finishTime"),
        @Index(name = "index_goodsOrder_goodsOrderNo",columnList = "goodsOrderNo",unique = true),
        @Index(name = "index_goodsOrder_pay",columnList = "pay"),
        @Index(name = "index_goodsOrder_orderStatus",columnList = "orderStatus"),
        @Index(name = "index_goodsOrder_trackingNo",columnList = "trackingNo",unique = true),
        @Index(name = "index_goodsOrder_refund",columnList = "refund"),
        @Index(name = "index_goodsOrder_complaint",columnList = "complaint"),
        @Index(name = "index_goodsOrder_complaintDeal",columnList = "complaintDeal")
})
public class GoodsOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer uid;
    private String sid;
    private String gid;
    private Integer pid;
    private Long createTime;
    private Long takeTime;
    private Long finishTime;
    private Long cancelTime;

    /**
     * 商品订单号
     */
    private String goodsOrderNo;
    /**
     * 商品标题
     */
    private String goodsTitle;
    /**
     * 商品封面
     */
    private String cover;
    /**
     * 商品规格(多个逗号规格)
     */
    private String specOrderIds;
    /**
     * 运费
     */
    private BigDecimal freight;
    /**
     * 最终实付款
     */
    private BigDecimal finalPay;
    /**
     * 是否支付
     */
    private Boolean pay;
    /**
     * 订单状态
     */
    @Enumerated(EnumType.STRING)
    private OrderStatusEnum orderStatus;
    /**
     * 物流单号
     */
    private String trackingNo;
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
     * 退款单号
     */
    private String refundOrderNo;
    /**
     * 是否退款：由商家进行退款，全额退款可取消订单。
     */
    private Boolean refund;
    /**
     * 退款时间
     */
    private Boolean refundTime;
    /**
     * 退款金额
     */
    private Boolean refundMoney;

    /**
     * 是否投诉
     */
    private Boolean complaint;
    /**
     * 投诉时间
     */
    private String complaintTime;
    /**
     * 投诉原因(富文本)
     */
    private String complaintReasons;
    /**
     * 是否处理投诉
     */
    private Boolean complaintDeal;
}
