package com.jiwuzao.common.pojo.store;

import com.jiwuzao.common.domain.enumType.StoreStyleEnum;
import com.jiwuzao.common.include.valid.StringMax;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class StorePagePojo {

    private StoreStyleEnum storeStyle;

    private Integer pageNum = 0;

    private Integer pageSize = 100;

    @StringMax
    private String orderBy = "createTime";


}
