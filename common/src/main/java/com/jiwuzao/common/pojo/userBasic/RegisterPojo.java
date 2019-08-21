package com.jiwuzao.common.pojo.userBasic;

import com.jiwuzao.common.include.valid.MsCode;
import com.jiwuzao.common.include.valid.NickName;
import com.jiwuzao.common.include.valid.Password;
import com.jiwuzao.common.include.valid.Phone;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-25 16:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class RegisterPojo {
    @Phone
    private String phone;
    @NickName(require = false)
    private String nickName;
    @Password
    private String pwd;
    @MsCode
    private Integer msCode;
}
