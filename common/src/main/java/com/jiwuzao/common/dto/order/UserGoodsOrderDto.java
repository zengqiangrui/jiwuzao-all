package com.jiwuzao.common.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
/**
 * 对于商家端，传用户在本店铺下单的信息便于发货
 */
public class UserGoodsOrderDto {

    /**
     * 用户id
     */
    private Integer uid;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 订单信息
     */
    private List<GoodsOrderDto> goodsOrderDtos;
}
