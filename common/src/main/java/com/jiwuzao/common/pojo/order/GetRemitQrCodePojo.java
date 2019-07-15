package com.jiwuzao.common.pojo.order;

import com.jiwuzao.common.include.valid.IpAddress;
import com.jiwuzao.common.include.valid.MsCode;
import com.jiwuzao.common.include.valid.TrueName;
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
