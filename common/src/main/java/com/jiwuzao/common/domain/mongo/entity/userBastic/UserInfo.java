package com.jiwuzao.common.domain.mongo.entity.userBastic;

import com.jiwuzao.common.domain.enumType.SexEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 用户信息
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-13 13:10
 */
@Document
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    @Id
    private String id;
    @Indexed(unique = true)
    private Integer uid;
    @Indexed
    private Long createTime;

    /**
     * 唯一昵称
     */
    @Indexed(unique = true)
    private String nickName;
    /**
     * 头像url地址
     */
    private String portrait;
    /**
     * 性别:男 女 未知
     */
    @Indexed
    private SexEnum sex;
    /**
     * 生日
     */
    @Indexed
    private Long birthday;
    /**
     * 省份
     */
    @Indexed
    private String province;
    /**
     * 城市
     */
    @Indexed
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
