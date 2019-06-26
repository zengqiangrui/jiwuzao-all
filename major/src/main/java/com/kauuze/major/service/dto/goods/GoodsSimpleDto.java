package com.kauuze.major.service.dto.goods;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-06-09 15:50
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class GoodsSimpleDto {
    private String gid;
    /**
     * 标题
     */
    private String title;
    /**
     * 封面
     */
    private String cover;
    /**
     * 销量
     */
    private Integer salesVolume;
    /**
     * 默认价格
     */
    private BigDecimal defaultPrice;
    /**
     * 邮费
     */
    private BigDecimal postage;
}
