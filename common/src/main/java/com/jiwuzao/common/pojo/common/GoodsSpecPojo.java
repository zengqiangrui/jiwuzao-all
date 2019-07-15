package com.jiwuzao.common.pojo.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-06-04 11:10
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class GoodsSpecPojo {
    /**
     * 规格分类(逗号分隔)
     */
    private String specClass;
    /**
     * 规格价格
     */
    private BigDecimal specPrice;
    /**
     * 规格库存
     */
    private Integer specInventory;
}
