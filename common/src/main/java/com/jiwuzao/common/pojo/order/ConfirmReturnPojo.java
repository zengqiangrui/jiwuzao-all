package com.jiwuzao.common.pojo.order;

import com.jiwuzao.common.include.valid.Password;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class ConfirmReturnPojo {
    @NotNull
    private Integer id;

    @Password
    private String password;


}
