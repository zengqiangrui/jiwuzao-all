package com.jiwuzao.common.vo.order;

import com.jiwuzao.common.include.valid.StringMax;
import com.jiwuzao.common.pojo.common.PagePojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class StoreOrderPagePojo {

    @StringMax(require = false)
    private String storeId;

    @StringMax(require = false)
    private String storeName;

    private PagePojo page;
}
