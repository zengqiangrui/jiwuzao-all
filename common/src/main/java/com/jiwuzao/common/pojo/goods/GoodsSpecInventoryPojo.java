package com.jiwuzao.common.pojo.goods;

import com.jiwuzao.common.include.valid.Decimal;
import com.jiwuzao.common.include.valid.StringMax;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class GoodsSpecInventoryPojo {
    @StringMax
    private String gid;
    @StringMax
    private String goodsSpecId;

    private Integer inventory;
}
