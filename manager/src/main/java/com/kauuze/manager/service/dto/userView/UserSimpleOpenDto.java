package com.kauuze.manager.service.dto.userView;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用户简单资料
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-23 18:36
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserSimpleOpenDto {
    /**
     * 用户id
     */
    private Integer uid;
    /**
     * 唯一昵称
     */
    private String nickName;
    /**
     * 头像
     */
    private String portrait;
    /**
     * 电话号码
     */
    private String phone;
}
