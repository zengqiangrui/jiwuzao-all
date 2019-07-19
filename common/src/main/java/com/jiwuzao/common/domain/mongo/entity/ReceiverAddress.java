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
 * 收货地址
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-13 21:42
 */
@Document
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ReceiverAddress {
    @Id
    private String id;
    @Indexed
    private Integer uid;

    /**
     * 默认收货省,市,区
     */
    private String receiveProvinces;
    /**
     *默认收货详细地址
     */
    private String receiverAddress;

    /**
     * 默认收货手机
     */
    private String receiverPhone;

    /**
     * 默认收货姓名
     */
    private String receiverTrueName;

    /**
     * 枚举，地址状态,默认为普通地址
     */
    private AddressEnum addressStatus = AddressEnum.USUAL;

    private Long createTime;

    private Long updateTime;
}
