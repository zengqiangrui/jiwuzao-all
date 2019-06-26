package com.kauuze.major.service.dto.userBasic;

import com.kauuze.major.domain.enumType.RoleEnum;
import com.kauuze.major.domain.enumType.SexEnum;
import com.kauuze.major.domain.enumType.UserStateEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用户所有公开资料
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-23 18:36
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserOpenDto {
    private Integer uid;
    /**
     * 注册时间
     */
    private Long createTime;
    /**
     * 唯一昵称
     */
    private String nickName;
    /**
     * 头像url地址
     */
    private String portrait;
    /**
     * 性别
     */
    private SexEnum sex;
    /**
     * 生日
     */
    private Long birthday;
    /**
     * 省份
     */
    private String province;
    /**
     * 城市
     */
    private String city;
    /**
     *个性签名
     */
    private String personalSign;
    /**
     * 公开微信号
     */
    private String openWxId;
    /**
     * 公开qq号
     */
    private String openQQ;

    /**
     *
     * 前台角色
     */
    private RoleEnum role;
    /**
     * 是否为vip用户
     */
    private Boolean vip;

    /**
     * 状态 normal--正常状态 shutup--禁言状态 ban--禁用状态
     * 管理员不受影响
     */
    private UserStateEnum userState;

    /**
     * 最后登录时间
     */
    private Long lastLoginTime;
    /**
     * 最后访问时间
     */
    private Long lastAccessTime;
    /**
     * 店铺正常经营则有
     */
    private String sid;
}
