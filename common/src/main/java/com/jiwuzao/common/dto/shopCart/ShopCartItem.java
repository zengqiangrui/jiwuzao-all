package com.jiwuzao.common.dto.shopCart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ShopCartItem {
    private String cid;//shopcart id
    private String goodsId;//goods id
    private String sid;//store id
    private String gsid;//规格id
    /**
     * 商品选中状态
     */
    private Boolean checkStatus = false;

    /**
     * 发货承诺
     */
    private String goodsDeliverPromise;
    /**
     * 退换承诺
     */
    private String goodsReturnPromise;

    /**
     * 商品显示标题
     */
    private String goodsName;

    /**
     * 商品显示图片
     */
    private String goodsImg;

    /**
     * 商品显示规格
     */
    private String goodsSpec;

    /**
     * 商品价格
     */
    private String goodsPrice;

    /**
     * 商品运费
     */
    private String goodsPostage;

    /**
     * 商品数量
     */
    private Integer goodsNumber;

    /**
     * 商品库存
     */
    private Integer goodsStock;
}
