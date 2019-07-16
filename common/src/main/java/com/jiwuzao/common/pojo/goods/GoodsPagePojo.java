package com.jiwuzao.common.pojo.goods;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class GoodsPagePojo {

    /**
     * 每页容量
     */
    private Integer pageSize = 10;

    /**
     * 当前页
     */
    private Integer currentPage = 0;

    /**
     * 是否是升序排列，默认是
     */
    private Boolean isAsc = true;

    /**
     * 根据数据库中的字段排列默认为时间排序
     */
    private String sortBy = "createTime";


}
