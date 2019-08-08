package com.jiwuzao.common.pojo.common;

import com.jiwuzao.common.include.valid.Idv;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class VhidPojo {
    @Idv
    private String vhid;
}
