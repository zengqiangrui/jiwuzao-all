package com.kauuze.major.api.pojo.goods;

import com.kauuze.major.api.pojo.common.GoodsSpecPojo;
import com.kauuze.major.domain.enumType.GoodsClassifyEnum;
import com.kauuze.major.include.valid.*;
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
    @StringMax(max = 40)
    private String title;
    @Url
    private String cover;
    @Decimal
    private BigDecimal defaultPrice;
    @Urls
    private String slideshow;
    @Decimal(zero = true)
    private BigDecimal postage;
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
