package com.kauuze.manager.service.dto.storeView;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class StoreShowDto {
    /**
     * 店铺id
     */
    private String id;
    /**
     * 匠人uid
     */
    private Integer uid;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 是否违规
     */
    private Boolean violation;
    /**
     * 违规原因
     */
    private String violationCause;
    /**
     * 店铺名称
     */
    private String storeName;
    /**
     * 店铺图标
     */
    private String storeIcon;
    /**
     * 客服电话
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
