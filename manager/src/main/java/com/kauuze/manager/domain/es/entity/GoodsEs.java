package com.kauuze.manager.domain.es.entity;


import com.jiwuzao.common.domain.enumType.AuditTypeEnum;
import com.jiwuzao.common.domain.enumType.GoodsClassifyEnum;
import com.kauuze.manager.ConfigUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.mongodb.core.index.Indexed;

import java.math.BigDecimal;

/**
 * 商品搜索
 */
@Document(indexName = "goods_" + ConfigUtil.customEnvironment,type = "_doc")
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class GoodsEs {
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
    @Field(analyzer = "ik",searchAnalyzer = "ik")
    private String title;
    /**
     * 封面
     */
    private String cover;
    /**
     * 分类
     */
    @Indexed
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