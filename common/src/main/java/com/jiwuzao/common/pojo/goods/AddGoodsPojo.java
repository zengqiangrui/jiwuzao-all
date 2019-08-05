package com.jiwuzao.common.pojo.goods;

import com.jiwuzao.common.domain.enumType.GoodsClassifyEnum;
import com.jiwuzao.common.include.valid.*;
import com.jiwuzao.common.pojo.common.GoodsSpecPojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-06-04 13:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AddGoodsPojo {
    @NotNull
    private GoodsClassifyEnum goodsClassify;
    @NotNull
    private String goodsClassDetail;
    @StringMax(max = 40)
    private String title;
    @Url
    private String cover;
    @Decimal
    private BigDecimal defaultPrice;
    @Urls
    private String slideshow;
    @Decimal(zero = true)
    private BigDecimal postage = BigDecimal.ZERO;
    @StringMax
    private String detailLabel;
    @StringMax
    private String goodsType;
    @StringMax(max = 1000)
    private String goodsTypeClass;
    @ListSizeMax(max = 200)
    private List<GoodsSpecPojo> goodsSpecPojo;
    @Urls
    private String detailPhotos;
}
