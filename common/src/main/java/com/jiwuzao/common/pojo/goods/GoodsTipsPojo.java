package com.jiwuzao.common.pojo.goods;

import com.jiwuzao.common.include.valid.StringMax;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
/**
 * 根据商品搜索。
 */
public class GoodsTipsPojo {
    @StringMax
    private String tips;
}
