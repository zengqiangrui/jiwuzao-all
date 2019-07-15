package com.jiwuzao.common.pojo.userBasic;

import com.jiwuzao.common.include.valid.NickName;
import com.jiwuzao.common.include.valid.Page;
import com.jiwuzao.common.pojo.common.PagePojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-27 15:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SearchByNickNamePojo {
    @NickName
    private String nickName;
    @Page
    private PagePojo page;
}
