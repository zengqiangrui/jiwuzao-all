package com.jiwuzao.common.domain.common;

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
public class SearchGoodsSimpleDto {
    private String gid;
    /**
     * 商品标题
     */
    private String title;
    /**
     * 商品封面
     */
    private String cover;
    /**
     * 商品默认价格
     */
    private BigDecimal defaultPrice;
    /**
     * 邮费
     */
    private BigDecimal postage;
    /**
     * 商品价格
     */
    private Integer salesVolume;

}
