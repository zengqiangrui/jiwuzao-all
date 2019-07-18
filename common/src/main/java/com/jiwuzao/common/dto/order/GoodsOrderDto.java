package com.jiwuzao.common.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class GoodsOrderDto {
    //Todo将详情字段放入该Dto
    private String gid;
}
