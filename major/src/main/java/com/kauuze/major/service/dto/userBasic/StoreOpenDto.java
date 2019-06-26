package com.kauuze.major.service.dto.userBasic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-06-08 17:24
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class StoreOpenDto {
    private String sid;
    private Integer uid;
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
