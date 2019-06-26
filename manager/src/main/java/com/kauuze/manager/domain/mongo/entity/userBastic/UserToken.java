package com.kauuze.manager.domain.mongo.entity.userBastic;


import com.kauuze.manager.domain.enumType.BackRoleEnum;
import com.kauuze.manager.domain.enumType.RoleEnum;
import com.kauuze.manager.domain.enumType.UserStateEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * 用户令牌
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-07 22:04
 */
@Document
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserToken {
    @Id
    private String id;
    @Indexed(unique = true)
    private Integer uid;

    /**
     * 前台角色
     */
    @Indexed
    private RoleEnum role;

    /**
     *后台角色
     */
    @Indexed
    private BackRoleEnum backRole;

    /**
     * 是否为vip用户
     */
    @Indexed
    private Boolean vip;
    /**
     * vip最后期限
     */
    @Indexed
    private Long vipEndTime;
    /**
     * 访问令牌 uid+","+32-64位,每次登陆后都会变化。过期让用户重登
     */
    @Indexed(unique = true)
    private String accessToken;

    /**
     * 状态 normal--正常状态 shutup--禁言状态 ban--禁用状态
     * 管理员不受影响
     */
    @Indexed
    private UserStateEnum userState;
    /**
     * 状态最后期限，过期变成normal。
     */
    @Indexed
    private Long userStateEndTime;
    /**
     * 最后登录时间
     */
    @Indexed
    private Long lastLoginTime;
    /**
     * 最后访问时间
     */
    @Indexed
    private Long lastAccessTime;

}
