package com.jiwuzao.common.pojo.order;

import com.jiwuzao.common.include.valid.Idv;
import com.jiwuzao.common.include.valid.StringMax;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AskServicePojo {
    @Idv
    private String goodsOrderNo;
    /**
     * 用户反馈内容
     */
    @StringMax
    private String content;
}
