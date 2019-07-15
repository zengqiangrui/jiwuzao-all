package com.jiwuzao.common.domain.mongo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

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
    @Indexed(unique = true)
    private Integer uid;

    /**
     * 默认收货省份
     */
    private String defaultReceiverProvince;
    /**
     * 默认收货城市
     */
    private String defaultReceiverCity;
    /**
     *默认收货详细地址
     */
    private String defaultReceiverAddress;

    /**
     * 默认收货手机
     */
    private String defaultReceiverPhone;

    /**
     * 默认收货姓名
     */
    private String defaultReceiverTrueName;
}
