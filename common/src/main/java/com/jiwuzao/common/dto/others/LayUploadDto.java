package com.jiwuzao.common.dto.others;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class LayUploadDto {


    private Integer code;

    private String msg;

    private LayImgDto Data;
}
