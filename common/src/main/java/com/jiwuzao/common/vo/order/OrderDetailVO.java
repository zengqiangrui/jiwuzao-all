package com.jiwuzao.common.vo.order;

import com.jiwuzao.common.domain.enumType.OrderExStatusEnum;
import com.jiwuzao.common.domain.enumType.OrderStatusEnum;
import com.jiwuzao.common.domain.enumType.PayChannelEnum;
import com.jiwuzao.common.domain.mysql.entity.GoodsOrder;
import com.jiwuzao.common.exception.excEnum.OrderExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class OrderDetailVO {

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

    //规格id
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
    private Long createTime;//下单时间

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

    /**
     * 物流单号
     */
    private String expressNo;
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
     * 记录异常订单原因
     */
    @Enumerated(EnumType.STRING)
    private OrderExceptionEnum exceptionReason;//订单异常原因
    private String openid;//微信openid
    private BigDecimal afterFee;

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
     * 预支付会话标识
     */
    private String prepayId;
    /**
     * 微信支付订单号
     */
    private String transactionId;

    private String storeName;
    /**
     * 店铺图标
     */
    private String storeIcon;
    /**
     * 客服电话:短信确认
     */
    private String servicePhone;
}
