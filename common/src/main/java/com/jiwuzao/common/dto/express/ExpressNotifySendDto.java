package com.jiwuzao.common.dto.express;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Data
@Accessors(chain = true)
public class ExpressNotifySendDto {
    @JsonProperty("EBusinessID")
    private String eBusinessID;

    @JsonProperty(value = "UpdateTime")
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss")
    private Date updateTime;

    @JsonProperty("Success")
    private Boolean success;

    @JsonProperty("Reason")
    private String reason;
}
