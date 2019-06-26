package com.kauuze.order.domain.common;

import com.kauuze.order.domain.enumType.GoodsClassifyEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-29 15:01
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SearchGoodsDto {
    private String gid;
    private Integer sid;
    /**
     * 商品标题
     */
    private String title;
    /**
     * 商品分类
     */
    private GoodsClassifyEnum classify;
    /**
     * 销量
     */
    private Integer salesVolume;
    /**
     * 默认价格
     */
    private BigDecimal defaultPrice;
    /**
     * 店铺名称
     */
    private String storeName;
    /**
     * 店铺图标
     */
    private String storeIcon;
}
