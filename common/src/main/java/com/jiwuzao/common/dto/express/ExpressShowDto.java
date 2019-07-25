package com.jiwuzao.common.dto.express;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ExpressShowDto {

    private String orderNo;//订单号

    private String expNo;//运单号

    private Long createTime;//创建时间

    private List<ExpressTraceDto> traces;//轨迹
}
