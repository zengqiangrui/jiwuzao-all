package com.jiwuzao.common.vo.order;

import com.jiwuzao.common.dto.express.ExpressShowDto;
import com.jiwuzao.common.dto.express.ExpressTraceDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ExpressReturnShowVO {
    private String orderNo;//订单号

    private String expCompany;//快递公司

    private String expNo;//运单号

    private Long createTime;//创建时间

    private List<ExpressTraceDto> traces;//轨迹
    private String goodsReturnNo;

    private String reason;

    private String img;
}
