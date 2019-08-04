package com.jiwuzao.common.vo.goods;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ViewHistoryVO {
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
}
