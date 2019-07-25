package com.jiwuzao.common.dto.express;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ExpressResultDto {
    /**
     * 用户id
     */
    @JsonProperty("EBusinessID")
    private String eBusinessID;

    /**
     * 物流运单号
     */
    @JsonProperty("LogisticCode")
    private String logisticCode;

    /**
     * 快递公司编码，必须
     * @see com.jiwuzao.common.domain.mongo.entity.Express
     */
    @JsonProperty("ShipperCode")
    private String shipperCode;

    /**
     * 订单编号，非必须
     */
    @JsonProperty("OrderCode")
    private String orderCode;

    /**
     * 成功与否
     */
    @JsonProperty("Success")
    private Boolean success;

    @JsonProperty("Reason")
    private String reason;

    /**
     * 物流状态：2-在途中,3-签收,4-问题件
     */
    @JsonProperty("State")
    private String state;

    @JsonProperty("Traces")
    private List<ExpressTraceDto> traces;

}
