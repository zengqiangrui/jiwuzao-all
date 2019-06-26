package com.kauuze.major.api.pojo.common;

import com.kauuze.major.include.valid.NickName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-07 17:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class NickNamePojo {
    @NickName
    private String nickName;
}
