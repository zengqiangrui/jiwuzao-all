package com.jiwuzao.common.pojo.common;


import com.jiwuzao.common.include.valid.StringMax;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-17 15:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SuffixPojo {
    @StringMax(max = 10)
    private String suffix;
}
