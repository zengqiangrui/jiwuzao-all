package com.kauuze.major.api.pojo.userBasic;

import com.kauuze.major.include.valid.MsCode;
import com.kauuze.major.include.valid.NickName;
import com.kauuze.major.include.valid.Password;
import com.kauuze.major.include.valid.Phone;
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
    @NickName
    private String nickName;
    @Password
    private String pwd;
    @MsCode
    private Integer msCode;
}
