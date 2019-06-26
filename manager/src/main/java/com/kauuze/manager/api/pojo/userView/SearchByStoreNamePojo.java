package com.kauuze.manager.api.pojo.userView;

import com.kauuze.manager.api.pojo.common.PagePojo;
import com.kauuze.manager.include.valid.NickName;
import com.kauuze.manager.include.valid.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-22 16:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SearchByStoreNamePojo {
    @NickName
    private String storeName;
    @Page
    private PagePojo page;
}
