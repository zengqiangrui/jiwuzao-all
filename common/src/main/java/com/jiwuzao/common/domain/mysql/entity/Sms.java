package com.jiwuzao.common.domain.mysql.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-25 17:01
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(indexes = {
        @Index(name = "index_sms_phone",columnList = "phone",unique = true)
})
public class Sms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 6位验证码
     */
    private Integer code;
    /**
     * 过期时间:5分钟
     */
    private Long overTime;
    /**
     * 最大失败次数：3次
     */
    private Integer failCount;
}
