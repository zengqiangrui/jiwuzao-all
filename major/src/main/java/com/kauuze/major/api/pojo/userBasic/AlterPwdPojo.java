package com.kauuze.major.api.pojo.userBasic;

import com.kauuze.major.include.valid.MsCode;
import com.kauuze.major.include.valid.Password;
import com.kauuze.major.include.valid.Phone;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-26 20:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AlterPwdPojo {
    @Phone
    private String phone;
    @MsCode
    private Integer msCode;
    @Password
    private String newPwd;
}
