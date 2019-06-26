package com.kauuze.manager.api.pojo.common;


import com.kauuze.manager.include.valid.IpAddress;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-22 14:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class IpAddressPojo {
    @IpAddress
    private String ipAddress;
}
