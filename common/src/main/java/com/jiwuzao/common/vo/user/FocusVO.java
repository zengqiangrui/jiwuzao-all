package com.jiwuzao.common.vo.user;

import com.jiwuzao.common.domain.enumType.SexEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class FocusVO {
    private Integer uid;

    private String nickName;

    private String personalSign;

    private String avatar;

    private SexEnum sex;
    /*
       关注状态，如果是粉丝列表，就是我对粉丝的关注状态，
       如果是关注列表，就是对方对自己的关注状态
     */
    private Boolean status;
}
