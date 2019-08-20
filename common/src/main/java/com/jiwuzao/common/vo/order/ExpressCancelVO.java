package com.jiwuzao.common.vo.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ExpressCancelVO {
    private String goodsOrderNo;
    private String reason;
    private String type;
    private Long createTime;
}
