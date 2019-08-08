package com.jiwuzao.common.vo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class AppriseVO {
    /**
     * gid
     */
    private String gid;

    /**
     * 商品封面
     */
    private String cover;

    /**
     * 商品标题
     */
    private String title;

    /**
     * 商品默认价格
     */
    private BigDecimal price;
}
