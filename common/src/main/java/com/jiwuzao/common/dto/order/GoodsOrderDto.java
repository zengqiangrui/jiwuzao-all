package com.jiwuzao.common.dto.order;

import com.jiwuzao.common.domain.enumType.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class GoodsOrderDto {
    /**
     * 店铺id
     */
    private String sid;
    /**
     * 商品id
     */
    private String gid;
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
     * 收货人
     */
    private String receiverTrueName;
    /**
     * 收货人手机
     */
    private String receiverPhone;

    /**
     * 商品标题
     */
    private String goodsTitle;
    /**
     * 商品封面
     */
    private String cover;
    /**
     * 规格分类("红色;500ml;精装")
     */
    private String specString;
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
    /**
     * 订单状态
     */
    private OrderStatusEnum orderStatus;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 付款时间
     */
    private Long payTime;
    /**
     * 发货时间
     */
    private Long deliverTime;
    /**
     * 收货时间
     */
    private Long takeTime;
}
