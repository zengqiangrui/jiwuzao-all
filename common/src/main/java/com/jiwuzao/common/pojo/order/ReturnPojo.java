package com.jiwuzao.common.pojo.order;

import com.jiwuzao.common.include.valid.StringMax;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ReturnPojo {
    @NotNull
    private Integer id;

    @StringMax(require = false)
    private String reason;
}
