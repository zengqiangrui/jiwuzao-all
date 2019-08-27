package com.jiwuzao.common.vo.store;

import com.jiwuzao.common.domain.enumType.StoreStyleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class StoreSimpleVO {

    private String storeId;

    private StoreStyleEnum style;

    private String storeName;

    private String artisanName;

    private String storeIcon;

    private String description;
}
