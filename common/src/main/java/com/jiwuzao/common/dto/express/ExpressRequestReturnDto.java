package com.jiwuzao.common.dto.express;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ExpressRequestReturnDto {

    @JsonProperty("EBusinessID")
    private String eBusinessID;

    @JsonProperty("UpdateTime")
    private String updateTime;

    @JsonProperty("Success")
    private Boolean success;//是否成功

    @JsonProperty("Reason")
    private String reason;

    @JsonProperty("EstimatedDeliveryTime")
    private String estimatedDeliveryTime;
}
