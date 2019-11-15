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
public class ExpressResultTempDto {

    private String kddh;

    private String kdmc;

    private String message;

    private String errcode;

    private String status;

    private List<ExpressTempTraceDto> data;

}
