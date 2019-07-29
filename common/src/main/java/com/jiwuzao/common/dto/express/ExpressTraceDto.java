package com.jiwuzao.common.dto.express;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExpressTraceDto {
    @JsonProperty("AcceptTime")
    private String acceptTime;

    @JsonProperty("AcceptStation")
    private String acceptStation;

    @JsonProperty("Remark")
    private String remark;
}
