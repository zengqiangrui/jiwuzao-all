package com.jiwuzao.common.pojo.goods;

import com.jiwuzao.common.domain.enumType.GoodsClassifyEnum;
import com.jiwuzao.common.include.valid.StringMax;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class GoodsClassifyPojo {
    private GoodsClassifyEnum goodsClassify;
}
