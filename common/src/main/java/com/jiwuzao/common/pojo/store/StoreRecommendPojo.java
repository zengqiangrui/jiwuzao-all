package com.jiwuzao.common.pojo.store;

import com.jiwuzao.common.include.valid.StringMax;
import com.jiwuzao.common.include.valid.Urls;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class StoreRecommendPojo {

    @StringMax(require = false)
    private String storeId;

    @StringMax(require = false)
    private String storeName;

    @Urls
    private String images;
}
