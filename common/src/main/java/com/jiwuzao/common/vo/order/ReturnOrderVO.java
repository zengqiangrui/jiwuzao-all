package com.jiwuzao.common.vo.order;

import com.jiwuzao.common.domain.enumType.ReturnStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ReturnOrderVO {

    private Integer id;//退款订单id

    private String goodsOrderNo;//商品订单

    private String goodsReturnNo;//商品退款单号

    private String storeName;

    private String userName;

    private String userPhone;

    private String goodsTitle;

    private String goodsImg;

    private String returnReason;
//    private BigDecimal orderPrice;

    private ReturnStatusEnum returnStatus;

    private String returnPromise;
}
