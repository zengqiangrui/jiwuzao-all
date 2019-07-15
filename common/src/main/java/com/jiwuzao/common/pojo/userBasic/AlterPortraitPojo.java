package com.jiwuzao.common.pojo.userBasic;

import com.jiwuzao.common.include.valid.Url;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-27 21:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AlterPortraitPojo {
    @Url
    private String portrait;
}
