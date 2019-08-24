package com.jiwuzao.common.vo.goods;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class GoodsSimpleVO {

    private String goodsId;

    private String goodsName;

    private String goodsImg;

    private BigDecimal goodsPrice;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GoodsSimpleVO that = (GoodsSimpleVO) o;
        return goodsId.equals(that.goodsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(goodsId);
    }
}
