package com.jiwuzao.common.vo.goods;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class GoodsSimpleVO {

    private String goodsId;

    private String goodsName;

    private String goodsImg;

    private BigDecimal goodsPrice;
}
