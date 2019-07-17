package com.jiwuzao.common.pojo.shopcart;

import com.jiwuzao.common.include.valid.Idv;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AddItemPojo {
    @Idv
    private String gid;
    @Idv
    private String specId;
    @NotNull
    private Integer num;
}
