package com.jiwuzao.common.vo.goods;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class GoodsDetailVO {
    /**
     * 短视频或图片轮播最多5，分析连接
     */
    private String slideshow;
    /**
     *详情标签,最多5个,每个5字
     */
    private String detailLabel;

    /**
     * 详情图:可以6张
     */
    private String detailPhotos;

    /**
     * 标题
     */
    private String title;

    /**
     * 商品封面
     */
    private String cover;

    /**
     * 默认价格：可排序
     */
    private BigDecimal defaultPrice;

    /**
     * 邮费
     */
    private BigDecimal postage;

    /**
     * 匠人用户名
     */
    private String nickName;

    /**
     * 匠人头像url地址
     */
    private String portrait;

    /**
     * 商品规格分类
     */
    private String goodsType;

    /**
     * 商品类型分类
     */
    private String goodsTypeClass;

    /**
     * 商品点赞数
     */
    private Long appriseCnt;
}
