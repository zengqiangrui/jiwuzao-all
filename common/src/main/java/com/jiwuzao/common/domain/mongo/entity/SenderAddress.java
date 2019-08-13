package com.jiwuzao.common.domain.mongo.entity;

import com.jiwuzao.common.domain.enumType.AddressEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/**
 * 寄件地址，用于店铺物流信息跟踪的订阅
 */
@Document
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SenderAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String sid;//店铺id

    private String province;//省

    private String city;//市

    private String area;//区

    private String detail;//详细地址

    private String postCode;//邮编

    private String phone;//手机

    private String tel;//座机

    private String name;//寄件人姓名
    /**
     * 枚举，地址状态,默认为普通地址
     */
    private AddressEnum addressStatus = AddressEnum.USUAL;


}
