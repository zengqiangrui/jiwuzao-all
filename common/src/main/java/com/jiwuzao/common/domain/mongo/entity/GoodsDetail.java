package com.jiwuzao.common.domain.mongo.entity;

import com.jiwuzao.common.domain.enumType.GoodsClassifyEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 商品详情
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
     * 关联商品gid
     */
    private String gid;

    /**
     * 短视频或图片轮播最多5，分析连接
     */
    private String slideshow;

    /**
     *详情标签,最多5个,每个5字
     */
    private String detailLabel;

    /**
     * 商品一级分类
     */
    @Indexed
    private GoodsClassifyEnum goodsClassify;

    /**
     * 商品二级分类，暂时用逗号分隔。
     */
    @Indexed
    private String goodsSecondClassify;

    /**
     * 商品三级分类，用逗号分隔，即全部分类。
     */
    @Indexed
    private String goodsThirdClassify;
    /**
     * 商品规格分类
     */
    private String goodsType;
    /**
     * 商品类型分类
     */
    private String goodsTypeClass;
    /**
     * 详情图:可以6张
     */
    private String detailPhotos;
    /**
     * 持久化后的点赞数
     */
    private Long appriseCnt;
}
