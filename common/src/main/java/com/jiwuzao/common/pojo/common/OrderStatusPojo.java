package com.jiwuzao.common.pojo.common;

import com.jiwuzao.common.domain.enumType.OrderStatusEnum;
import com.jiwuzao.common.include.valid.StringMax;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class OrderStatusPojo{
    @StringMax(require = false)
    private String storeId;

    @StringMax(require = false)
    private String storeName;

    private OrderStatusEnum status;

    private PagePojo page;
}
