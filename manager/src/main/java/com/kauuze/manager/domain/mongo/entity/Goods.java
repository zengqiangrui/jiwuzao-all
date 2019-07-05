package com.kauuze.manager.domain.mongo.entity;


import com.kauuze.manager.ConfigUtil;
import com.kauuze.manager.domain.enumType.AuditTypeEnum;
import com.kauuze.manager.domain.enumType.GoodsClassifyEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * 商品搜索
 */
@Document
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Goods {
    /**
     * 商品详情id
     */
    @Id
    private String gid;
    private Integer uid;
    private String sid;

    /**
     * 标题
     */
    private String title;
    /**
     * 封面
     */
    private String cover;
    /**
     * 分类
     */
    @Indexed(unique = true)
    private GoodsClassifyEnum classify;
    /**
     * 评分:默认排序,排序必含
     */
    private Integer userScore;
    /**
     * 销量:可排序
     */
    private Integer salesVolume;
    /**
     * 默认价格：可排序
     */
    private BigDecimal defaultPrice;
    /**
     * 邮费
     */
    private BigDecimal postage;
    /**
     * 是否上架
     */
    private Boolean putaway;
    /**
     * 上架时间
     */
    private Long putawayTime;
    /**
     * 下架时间:72小时可删除
     */
    private Long soldOutTime;
    /**
     * 商品审核状态
     */
    private AuditTypeEnum auditType;
    /**
     * 拒绝原因
     */
    private String refuseCause;
}