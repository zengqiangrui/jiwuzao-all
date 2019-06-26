package com.kauuze.major.domain.mongo.entity.userBastic;

import com.kauuze.major.domain.enumType.AuditTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-06-10 15:03
 */
@Document
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class StoreAudit {
    @Id
    private String id;
    @Indexed
    private Integer uid;
    @Indexed
    private Long createTime;

    /**
     * 店铺名称:[违规+uid]
     */
    @Indexed
    private String storeName;
    /**
     * 店铺图标
     */
    private String storeIcon;

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

    @Indexed
    private AuditTypeEnum auditType;
    /**
     * 拒绝原因
     */
    private String refuseCause;
    /**
     * 审批时间
     */
    @Indexed
    private Long auditTime;
}
