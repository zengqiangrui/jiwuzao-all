package com.jiwuzao.common.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class FansAndFocusDTO {
    private Integer uid;

    private Integer focusNum = 0;

    private Integer fansNum = 0;
}
