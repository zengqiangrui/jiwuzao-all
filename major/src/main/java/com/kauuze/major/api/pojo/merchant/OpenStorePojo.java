package com.kauuze.major.api.pojo.merchant;

import com.kauuze.major.include.valid.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-06-10 11:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class OpenStorePojo {
    @NickName
    private String storeName;
    @Url
    private String storeIcon;
    @Phone
    private String servicePhone;
    @MsCode
    private Integer msCode;
    @StringMax
    private String storeIntro;
    @Url
    private String businessLicense;
}
