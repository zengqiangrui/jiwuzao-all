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
 * @time 2019-05-22 17:46
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