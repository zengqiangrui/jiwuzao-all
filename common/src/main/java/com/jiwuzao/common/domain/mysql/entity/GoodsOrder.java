package com.jiwuzao.common.domain.mysql.entity;


import com.jiwuzao.common.domain.enumType.OrderExStatusEnum;
import com.jiwuzao.common.domain.enumType.OrderStatusEnum;
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
        @Index(name = "index_goodsOrder_uid", columnList = "uid"),
        @Index(name = "index_goodsOrder_sid", columnList = "sid"),
        @Index(name = "index_goodsOrder_gid", columnList = "gid"),
        @Index(name = "index_goodsOrder_goodsOrderDetailId", columnList = "goodsOrderDetailId", unique = true),
        @Index(name = "index_goodsOrder_createTime", columnList = "createTime"),
        @Index(name = "index_goodsOrder_goodsOrderNo", columnList = "goodsOrderNo", unique = true),
        @Index(name = "index_goodsOrder_orderStatus", columnList = "orderStatus")
})
public class GoodsOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer uid;//用户id
    private Integer uid2;//匠人id
    private String sid;//店铺id
    private String gid;//商品id
    private Integer payid;
    private Integer goodsOrderDetailId;
    private Long createTime;

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
    //private String specOrderIds;
    /**
     * 商品规格id
     */
    private String gsid;
    /**
     * 规格分类
     */
    private String specClass;

    /**
     * 单价
     */
    private BigDecimal specPrice;

    /**
     * 购买数量
     */
    private Integer buyCount;

    /**
     * 运费
     */
    private BigDecimal postage;

    /**
     * 最终实付款
     */
    private BigDecimal finalPay;

    //该订单可提现金额，（finalPay-postage)*0.8
    private BigDecimal withdrawal;

    //商家是否可提现，默认不可提现，在成功支付15天后无异常可提现，定时任务实现（异常包含）
    private Boolean canRemit = false;

    //是否开发票
    private Boolean isReceipt = false;

    //今日是否催单
    private Boolean isHastened = false;

    private Boolean isAppraised = false;

    /**
     * 订单状态
     */
    @Enumerated(EnumType.STRING)
    private OrderStatusEnum orderStatus;

    /**
     * 订单异常状态
     */
    @Enumerated(EnumType.STRING)
    private OrderExStatusEnum orderExStatus = OrderExStatusEnum.normal;

    /**
     * 发货时间
     */
    private Long deliverTime;

    /**
     * 收货时间
     */
    private Long takeTime;

    /**
     * 退款时间
     */
    private Long refundTime;


}
