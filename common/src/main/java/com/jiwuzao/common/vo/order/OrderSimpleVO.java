package com.jiwuzao.common.vo.order;

import com.jiwuzao.common.domain.enumType.OrderExStatusEnum;
import com.jiwuzao.common.domain.enumType.OrderStatusEnum;
import com.jiwuzao.common.domain.mysql.entity.GoodsOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class OrderSimpleVO {

    private String goodsOrderNo;

    private String goodsTitle;

    private String specClass;

    private BigDecimal specPrice;

    /**
     * 商品封面
     */
    private String cover;
    /**
     * 购买数量
     */
    private Integer buyCount;
    /**
     * 订单状态
     */
    @Enumerated(EnumType.STRING)
    private OrderStatusEnum orderStatus;
    /**
     * 订单异常状态
     */
    @Enumerated(EnumType.STRING)
    private OrderExStatusEnum orderExStatus;

    /**
     * 创建时间
     */
    private Long createTime;



}
