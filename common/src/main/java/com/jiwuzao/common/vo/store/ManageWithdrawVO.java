package com.jiwuzao.common.vo.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ManageWithdrawVO {

    private Integer withDrawId;
    private Integer uid;
    private String storeId;

    private String storeName;

    private BigDecimal withdrawl;//todo 提现金额

    private String createTime;
}
