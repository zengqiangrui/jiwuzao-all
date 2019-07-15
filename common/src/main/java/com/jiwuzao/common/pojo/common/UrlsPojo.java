package com.jiwuzao.common.pojo.common;

import com.jiwuzao.common.include.valid.Urls;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-06-06 11:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UrlsPojo {
    @Urls
    private String urls;
}
