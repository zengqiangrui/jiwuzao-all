package com.jiwuzao.common.pojo.store;

import com.jiwuzao.common.domain.enumType.WithdrawStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class WithdrawSearchPojo {

    private WithdrawStatusEnum status;
}
