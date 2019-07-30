package com.jiwuzao.common.pojo.common;

import com.jiwuzao.common.include.valid.Idv;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class QuerySpecPojo {
    //规格字符串
    private String specClass;

    @Idv
    private String gid;
}
