package com.jiwuzao.common.pojo.order;

import com.jiwuzao.common.include.valid.StringMax;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ExpressPojo {
    @StringMax(max = 32)
    private String expCode;
    @StringMax(max = 32)
    private String orderId;
    @StringMax(max = 32)
    private String expNo;
}
