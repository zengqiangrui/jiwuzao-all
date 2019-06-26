package com.kauuze.order.domain.mysql.entity;


import com.kauuze.order.domain.enumType.PayChannelEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 支付时立即生成
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-13 13:40
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(indexes = {
        @Index(name = "index_payOrder_uid",columnList = "uid"),
        @Index(name = "index_payOrder_createTime",columnList = "createTime"),
        @Index(name = "index_payOrder_overtime",columnList = "overtime"),
        @Index(name = "index_payOrder_payOrderNo",columnList = "payOrderNo",unique = true),
        @Index(name = "index_payOrder_systemGoods",columnList = "systemGoods"),
        @Index(name = "index_payOrder_payTime",columnList = "payTime"),
        @Index(name = "index_payOrder_payChannel",columnList = "payChannel"),
        @Index(name = "index_payOrder_pay",columnList = "pay"),
        @Index(name = "index_payOrder_transactionId",columnList = "transactionId")

})
public class PayOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer uid;
    private Long createTime;
    private String payOrderNo;
    /**
     * 是否二维码支付:二维码支付不再次显示
     */
    private Boolean qrCode;
    /**
     * 是否超时:超1小时订单不显示但可接受回调,25小时后才删除
     */
    private Boolean overtime;

    /**
     * 是否为系统商品:系统商品不显示待支付,每次重新生成订单
     */
    private Boolean systemGoods;
    /**
     * 1小时未支付删除所有商品订单
     */
    private String goodsOrderIds;
    /**
     * 系统商品Id
     */
    private String systemGoodsId;
    /**
     * 最终实付款
     */
    private BigDecimal finalPay;
    /**
     * 订单备注
     */
    private String remark;

    /**
     * 支付回调时间
     */
    private Long payTime;
    /**
     * 支付渠道：微信支付
     */
    @Enumerated(EnumType.STRING)
    private PayChannelEnum payChannel;
    /**
     * 是否支付
     */
    private Boolean pay;
    /**
     * 外部交易号
     */
    private String transactionId;
    /**
     * 预支付会话标识
     */
    private String prepayId;
}
