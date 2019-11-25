package com.jiwuzao.common.pojo.goods;

import com.jiwuzao.common.domain.enumType.GoodsClassifyEnum;
import com.jiwuzao.common.include.valid.StringMax;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class GoodsCategoryPojo {
    //一级分类
    private GoodsClassifyEnum goodsClassify;
    //二级分类
    @StringMax(require = false)
    private String secondCategory;
    //三级分类
    @StringMax(require = false)
    private String thirdCategory;
    //当前页
    private Integer num;
    //当前页数量
    private Integer size;

}
