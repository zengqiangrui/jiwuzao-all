package com.jiwuzao.common.pojo.merchant;

import com.jiwuzao.common.domain.enumType.StoreStyleEnum;
import com.jiwuzao.common.include.valid.*;
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
    @Url
    private String storeBgImg;
    @Phone
    private String servicePhone;
    @MsCode
    private Integer msCode;
    @StringMax(max = 8192)
    private String storeIntro;
    @StringMax
    private StoreStyleEnum storeStyle;

}
