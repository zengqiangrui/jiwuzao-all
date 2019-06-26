package com.kauuze.major.api.pojo.system;

import com.kauuze.major.include.valid.StringMax;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-21 09:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AppVersionPojo {
    @StringMax
    private String currentVersion;
}
