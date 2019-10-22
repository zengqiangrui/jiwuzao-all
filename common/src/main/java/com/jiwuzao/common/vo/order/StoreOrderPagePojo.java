package com.jiwuzao.common.vo.order;

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

    private String storeId;

    private String storeName;

    private PagePojo page;
}
