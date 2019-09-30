package com.jiwuzao.common.pojo.order;

import com.jiwuzao.common.include.valid.Idv;
import com.jiwuzao.common.include.valid.StringMax;
import com.jiwuzao.common.include.valid.Url;
import com.jiwuzao.common.include.valid.Urls;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class GoodsReturnPojo {
    @Idv
    private String goodsOrderNo;

    @StringMax
    private String returnContent;

    @Url
    private String image;
}
