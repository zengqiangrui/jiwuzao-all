package com.jiwuzao.common.pojo.order;

import com.jiwuzao.common.domain.enumType.ReceiptEnum;
import com.jiwuzao.common.include.valid.StringMax;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ReceiptPojo {
    private Boolean isReceipt;//是否开局发票

    private ReceiptEnum type;//发票类型

    @StringMax(require = false)//主体名称
    private String name;

    @StringMax(require = false,max = 48)//纳税人id
    private String taxId;
}
