package com.jiwuzao.common.pojo.express;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jiwuzao.common.dto.express.ExpressTraceDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ExpressPushDataPojo {

    @JsonProperty("LogisticCode")
    private String logisticCode;

    @JsonProperty("ShipperCode")
    private String shipperCode;

    @JsonProperty("Traces")
    private List<ExpressTraceDto> traces;

    /**
     * 物 流 状 态 ：
     * 0-无 轨 迹
     * 1-已揽收
     * 2-在途中
     * 3-签收
     * 4-问题件
     */
    @JsonProperty("State")
    private String state;

    @JsonProperty("EBusinessID")
    private String eBusinessID;

    @JsonProperty("Success")
    private Boolean success;

    @JsonProperty("Reason")
    private String Reason;

    @JsonProperty("CallBack")
    private String callBack;

    @JsonProperty("EstimatedDeliveryTime")
    private String EstimatedDeliveryTime;
}
