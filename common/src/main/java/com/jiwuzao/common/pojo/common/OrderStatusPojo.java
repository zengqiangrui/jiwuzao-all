package com.jiwuzao.common.pojo.common;

import com.jiwuzao.common.domain.enumType.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class OrderStatusPojo {
    private OrderStatusEnum status;
}
