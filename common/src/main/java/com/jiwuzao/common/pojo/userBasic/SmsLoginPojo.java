package com.jiwuzao.common.pojo.userBasic;

import com.jiwuzao.common.include.valid.MsCode;
import com.jiwuzao.common.include.valid.Phone;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class SmsLoginPojo {
    @Phone
    private String phone;

    @Pattern(regexp = "^\\d{6}$", message = "验证码为6位数字")
    private String code;
}
