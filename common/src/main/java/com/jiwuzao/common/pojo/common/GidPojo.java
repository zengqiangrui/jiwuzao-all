package com.jiwuzao.common.pojo.common;

import com.jiwuzao.common.include.valid.Idv;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-23 09:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class GidPojo {
    @Idv
    private String gid;
}
