package com.kauuze.manager.api.pojo.userView;

import com.jiwuzao.common.pojo.common.PagePojo;
import com.kauuze.manager.include.valid.NickName;
import com.kauuze.manager.include.valid.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-30 20:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SearchByVerifyNamePojo {
    @NickName
    private String verifyName;
    @Page
    private PagePojo page;
}
