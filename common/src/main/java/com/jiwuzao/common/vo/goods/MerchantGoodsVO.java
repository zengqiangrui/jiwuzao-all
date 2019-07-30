package com.jiwuzao.common.vo.goods;

import com.jiwuzao.common.domain.enumType.GoodsClassifyEnum;
import com.jiwuzao.common.domain.mongo.entity.GoodsSpec;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class MerchantGoodsVO {

    //商品id
    private String gid;

    //商品标题
    private String title;

    //封面
    private String cover;

    //分类
    private GoodsClassifyEnum classify;

    //默认价格
    private BigDecimal defaultPrice;

    //邮费
    private BigDecimal postage;

    //是否上架
    private Boolean putAway;

    private String slideShow;//轮播图

    private String detailLabel;//详情标签

    private String detailPhotos;//详情图

    private List<GoodsSpec> goodsSpecs;//商品规格

}
