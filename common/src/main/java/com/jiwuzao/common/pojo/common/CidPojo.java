package com.jiwuzao.common.pojo.common;


import com.jiwuzao.common.include.valid.Idv;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Cid传参
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-02-24 12:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CidPojo {
    @Idv
    private String cid;
}
