package com.jiwuzao.common.dto.express;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExpressTempTraceDto {

    private String content;
    private String time;
}
