package com.jiwuzao.common.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ExpressRequestMainDto {

    @JsonProperty("RequestData")
    private ExpressRequestDataDto requestData;

    @JsonProperty("EBusinessID")
    private String eBusinessID;

    @JsonProperty("RequestType")
    private String requestType;

    @JsonProperty("DataSign")
    private String dataSign;

    @JsonProperty("DataType")
    private String dataType; //请求返回数据格式，2为json

}
