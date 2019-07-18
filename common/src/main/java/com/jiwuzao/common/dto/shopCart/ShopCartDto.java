package com.jiwuzao.common.dto.shopCart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ShopCartDto {
    /**
     * 店铺id
     */
    private String sid;

    /**
     * 店铺名称
     */
    private String storeName;

    private List<ShopCartItem> shopCartItem;
}
