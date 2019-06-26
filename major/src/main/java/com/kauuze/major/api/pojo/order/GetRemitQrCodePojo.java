package com.kauuze.major.api.pojo.order;

import com.kauuze.major.include.valid.IpAddress;
import com.kauuze.major.include.valid.MsCode;
import com.kauuze.major.include.valid.TrueName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-27 16:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class GetRemitQrCodePojo {
    @IpAddress
    private String ipAddress;
    @MsCode
    private Integer msCode;
    @TrueName
    private String wxRemitTrueName;
}
