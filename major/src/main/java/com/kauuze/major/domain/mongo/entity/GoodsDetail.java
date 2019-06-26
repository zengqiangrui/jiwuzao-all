package com.kauuze.major.domain.mongo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-13 23:44
 */
@Document
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class GoodsDetail {
    @Id
    private String id;
    /**
     * 短视频或图片轮播最多5，分析连接
     */
    private String slideshow;
    /**
     *详情标签,最多5个,每个5字
     */
    private String detailLabel;
    /**
     * 商品类型
     */
    private String goodsType;
    /**
     * 商品类型分类
     */
    private String goodsTypeClass;
    /**
     * 详情图:可以9张
     */
    private String detailPhotos;
}
