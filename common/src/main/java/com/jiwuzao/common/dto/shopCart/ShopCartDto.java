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

    private Integer uid;
    private String sid;

    /**
     * 
     */
    private String storeName;

    private List<ShopCartItem> shopCartItem;
}
