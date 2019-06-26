package com.kauuze.major.api.pojo.merchant;

import com.kauuze.major.include.valid.NickName;
import com.kauuze.major.include.valid.Url;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-06-10 11:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ModifyStoreNameAndStoreIconPojo {
    @NickName
    private String storeName;
    @Url
    private String storeIcon;
}
