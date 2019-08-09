package com.jiwuzao.common.vo.store;

import com.jiwuzao.common.domain.enumType.OpeningBankEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class StoreWithdrawVO {

    //店铺,用户id
    private String storeId;
    private Integer uid;

    //店铺名
    private String storeName;

    //银行卡号
    private Long bankNo;

    //真实姓名
    private String trueName;

    //开户行代号
    private OpeningBankEnum openingBank;

    //店铺可提现金额
    private BigDecimal withdrawAbleCash;

    /**
     * 上一次确认时间：
     * 店铺提现后由后台确认的最近一次司机
     */
//    private Long confirmTime;
}
