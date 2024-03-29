package com.jiwuzao.common.pojo.userBasic;

import com.jiwuzao.common.include.valid.IpAddress;
import com.jiwuzao.common.include.valid.TrueName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-06-01 17:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BankNoVerifyRemitQrCodePojo {
    @IpAddress
    private String ipAddress;
    @TrueName
    private String wxRemitTrueName;
}
