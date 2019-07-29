package com.jiwuzao.common.pojo.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class OrderPagePojo {

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

//    /**
//     * 是否是详情列表，默认否。
//     * 详情列表显示所有信息，效率可能比较低
//     */
//    private Boolean isDetail = false;

    /**
     * 根据数据库中的字段排列默认为时间排序
     */
    private String sortBy = "createTime";


}
