package com.jiwuzao.common.pojo.store;

import com.jiwuzao.common.include.valid.StringMax;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class StorePojo {
    @StringMax
    private String storeId;

    private Integer pageNum = 0;

    private Integer pageSize = 6;

    private String orderBy="createTime";
}
