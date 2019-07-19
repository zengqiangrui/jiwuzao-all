package com.jiwuzao.common.domain.mysql.entity;


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
        @Index(name = "index_goodsOrder_uid",columnList = "uid"),
        @Index(name = "index_goodsOrder_sid",columnList = "sid"),
        @Index(name = "index_goodsOrder_gid",columnList = "gid"),
        @Index(name = "index_goodsOrder_pid",columnList = "pid"),
        @Index(name = "index_goodsOrder_goodsOrderDetailId",columnList = "goodsOrderDetailId",unique = true),
        @Index(name = "index_goodsOrder_createTime",columnList = "createTime"),
        @Index(name = "index_goodsOrder_goodsOrderNo",columnList = "goodsOrderNo",unique = true),
        @Index(name = "index_goodsOrder_orderStatus",columnList = "orderStatus")
})
public class GoodsOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer uid;
    private String sid;
    private String gid;
    private String pid;
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
     * 规格分类
     */
    private String specClass;
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
    /**
     * 发货时间
     */
    private Long deliverTime;
    /**
     * 收货时间
     */
    private Long takeTime;
}
