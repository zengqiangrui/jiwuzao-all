package com.kauuze.order.domain.mongo.entity.userBastic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 店铺只能修改无法删除
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-06-08 14:12
 */
@Document
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Store {
    @Id
    private String id;
    @Indexed(unique = true)
    private Integer uid;
    @Indexed
    private Long createTime;

    /**
     * 是否违规：违规下架所有商品，不得再上架。
     */
    @Indexed
    private Boolean violation;
    /**
     * 违规原因：富文本
     */
    private String violationCause;
    /**
     * 封禁开始时间：封禁99年
     */
    private Long banStartTime;

    /**
     * 店铺名称:[违规+uid]
     */
    @Indexed(unique = true)
    private String storeName;
    /**
     * 店铺图标
     */
    private String storeIcon;
    /**
     * 店铺标识 一年最多修改2次，不得违规
     */
    private Integer storeNameAndIconModifyCount;
    /**
     * 客服电话:短信确认
     */
    private String servicePhone;
    /**
     * 店铺介绍
     */
    private String storeIntro;
    /**
     * 营业执照
     */
    private String businessLicense;
}
