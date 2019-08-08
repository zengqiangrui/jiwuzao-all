package com.jiwuzao.common.domain.mongo.entity;

import com.jiwuzao.common.domain.enumType.AddressEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 地址,可能是收货，也可以是发货地址
 */
@Document
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id
    private String id;
    @Indexed
    private Integer uid;

    /**
     * 默认收货省-市-区(以横线分割)
     */
    private String provinces;
    /**
     *默认详细地址
     */
    private String addressDetail;

    /**
     * 默认收货手机
     */
    private String phone;

    /**
     * 默认收货姓名
     */
    private String trueName;

    /**
     * 枚举，地址状态,默认为普通地址
     */
    private AddressEnum addressStatus = AddressEnum.USUAL;

    private Long createTime;

    private Long updateTime;
}
