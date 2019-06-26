package com.kauuze.manager.service.dto.userView;


import com.kauuze.manager.domain.enumType.BackRoleEnum;
import com.kauuze.manager.domain.enumType.RoleEnum;
import com.kauuze.manager.domain.enumType.SexEnum;
import com.kauuze.manager.domain.enumType.UserStateEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用户后台展示资料
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-23 17:03
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserShowDto {
    /**
     * 用户id
     */
    private Integer uid;
    /**
     * 用户绑定手机号
     */
    private String phone;
    /**
     *
     * 前台角色
     */
    private RoleEnum role;

    /**
     *后台角色
     */
    private BackRoleEnum backRole;

    /**
     * 是否为vip用户
     */
    private Boolean vip;
    /**
     * vip最后期限
     */
    private Long vipEndTime;
    /**
     * 状态 normal--正常状态 shutup--禁言状态 ban--禁用状态
     * 管理员不受影响
     */
    private UserStateEnum userState;
    /**
     * 状态最后期限，过期变成normal。
     */
    private Long userStateEndTime;

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
}
