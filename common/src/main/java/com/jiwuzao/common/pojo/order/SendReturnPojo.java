package com.jiwuzao.common.pojo.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class SendReturnPojo {
    private Integer id;

    /**
     * 快递公司编码
     */
    private String expCode;

    /**
     * 快递公司单号
     */
    private String expNo;
}
