package com.jiwuzao.common.vo.order;

import com.jiwuzao.common.domain.enumType.OrderStatusEnum;
import com.jiwuzao.common.pojo.common.PagePojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserOrderPojo {
    private Integer uid;

    private OrderStatusEnum status;

    private PagePojo page;
}
