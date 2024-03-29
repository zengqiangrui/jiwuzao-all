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
public class ViewHistoryVO {
    /**
     * vhid
     */
    private String vhid;
    /**
     * gid
     */
    private String gid;

    /**
     * 浏览时间
     */
    private Long time;

    /**
     * 商品名称
     */
    private String goodsTitle;

    /**
     * 商品封面
     */
    private String cover;

    /**
     * 商品默认价格
     */
    private BigDecimal price;
}
