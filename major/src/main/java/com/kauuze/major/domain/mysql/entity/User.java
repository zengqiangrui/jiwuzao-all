package com.kauuze.major.domain.mysql.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 全部信息初始化完毕，用户才注册成功
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-02-24 12:30
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(indexes = {
        @Index(name = "index_user_phone",columnList = "phone",unique = true),
        @Index(name = "index_user_todayWithdrawal",columnList = "todayWithdrawal")
})
public class User {
    /**
     * 用户uid 6-10位
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 绑定的手机号
     */
    private String phone;

    /**
     * 密码:SHA256(明文--8位带字母 + 密码盐)
     */
    private String pwd;

    /**
     * 密码盐32-64位
     */
    private String pwdSalt;

    /**
     * 密码登陆失败连续超过10次需要修改密码
     */
    private Integer pwdFailCount;
    /**
     * 商家今日是否提现：一天最多1次,每次1000以上
     */
    private Boolean todayWithdrawal;
    /**
     * 保证金
     */
    private BigDecimal deposit;
    /**
     * 用户可提现金额
     */
    private BigDecimal withdrawal;
    /**
     * 唯一昵称：在userinfo中冗余
     */
    private String nickName;
    /**
     * vip用户：在userToken中冗余
     */
    private Boolean vip;
    /**
     * vip最后期限：在userToken中冗余
     */
    private Long vipEndTime;
}
