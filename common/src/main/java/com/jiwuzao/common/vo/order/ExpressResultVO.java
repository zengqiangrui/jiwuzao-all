package com.jiwuzao.common.vo.order;

import com.jiwuzao.common.domain.enumType.ExpressEnum;
import com.jiwuzao.common.domain.enumType.ExpressStatusEnum;
import com.jiwuzao.common.dto.express.ExpressTraceDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ExpressResultVO {
    private String expCompany;//物流公司名称
    private String expNo;//物流订单编号
    private String orderNo;//商品订单号
    private String detailAddress;//详细退货地址
    private Boolean success;// 成功与否
    private String state;//物流状态
    private List<ExpressTraceDto> traces;//物流轨迹
}
