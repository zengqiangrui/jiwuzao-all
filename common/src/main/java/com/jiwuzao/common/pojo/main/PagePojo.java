package com.jiwuzao.common.pojo.main;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PagePojo {
    /**
     * 每页容量
     */
    private Integer pageSize = 10;

    /**
     * 当前页
     */
    private Integer currentPage = 0;

    private Long time = System.currentTimeMillis();// 毫秒级时间戳用于时间排序和比较
}
