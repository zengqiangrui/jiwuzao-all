package com.jiwuzao.common.pojo.userBasic;

import com.jiwuzao.common.include.valid.Password;
import com.jiwuzao.common.include.valid.Phone;
import com.jiwuzao.common.include.valid.StringMax;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-25 16:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class LoginPojo {
    @Phone
    private String phone;
    @Password
    private String pwd;
    @StringMax
    private String sha256;
}
