package com.jiwuzao.common.dto.order;

import com.jiwuzao.common.domain.enumType.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class GoodsOrderSimpleDto {
    /**
     * 店铺id
     */
    private String sid;
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
    private BigDecimal freight;
    /**
     * 最终实付款
     */
    private BigDecimal finalPay;
    /**
     * 订单状态
     */
    @Enumerated(EnumType.STRING)
    private OrderStatusEnum orderStatus;
}