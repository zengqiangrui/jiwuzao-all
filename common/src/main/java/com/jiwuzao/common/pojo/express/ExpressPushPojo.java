package com.jiwuzao.common.pojo.express;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ExpressPushPojo {

    @JsonProperty("PushTime")
    private String pushTime;

    @JsonProperty("EBusinessID")
    private String eBusinessID;

    @JsonProperty("Data")
    private List<ExpressPushDataPojo> data;

    @JsonProperty("Count")
    private Integer count;

}
