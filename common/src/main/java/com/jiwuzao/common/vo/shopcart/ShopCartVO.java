package com.jiwuzao.common.vo.shopcart;

import com.jiwuzao.common.dto.shopCart.ShopCartItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ShopCartVO {
    //这个bean用于给前端返回数据，参数的命名与前端保持一致，和后台参数不一致，需要后续统一命名
    /**
     * 店铺id
     */
    private String artisanId;
    /**
     * 店铺选中状态
     */
    private Boolean checkStatus = false;
    /**
     * 店铺名称
     */
    private String shopName;

    private List<ShopCartItem> goods;
}
