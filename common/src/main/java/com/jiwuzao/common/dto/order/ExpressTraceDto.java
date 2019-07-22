package com.jiwuzao.common.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ExpressTraceDto {
    @JsonProperty("AcceptTime")
    private String acceptTime;

    @JsonProperty("AcceptStation")
    private String acceptStation;

    @JsonProperty("Remark")
    private String remark;
}
