package com.kauuze.manager.domain.mongo.entity.userBastic;


import com.jiwuzao.common.domain.enumType.AuditTypeEnum;
import com.kauuze.manager.domain.enumType.OpeningBankEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 实名认证
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-13 21:50
 */
@Document
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class VerifyActor {
    @Id
    private String id;
    @Indexed(unique = true)
    private Integer uid;
    @Indexed
    private Long createTime;

    /**
     * 负责人真实姓名
     */
    private String trueName;
    /**
     * 负责人身份证号
     */
    @Indexed
    private String idcard;
    /**
     * 身份证正面
     */
    private String frontIdCardPhoto;
    /**
     * 身份证背面
     */
    private String backIdCardPhoto;
    /**
     * 手持身份证
     */
    private String handIdCardPhoto;

    /**
     * 对公银行卡号
     */
    private Long bankNo;
    /**
     *对公银行卡姓名
     */
    private String bankTrueName;
    /**
     * 银行卡开户行
     */
    private OpeningBankEnum openingBank;
    /**
     * 企业名称
     */
    private String companyName;
    /**
     * 统一社会信用代码:国家信息公示网查询
     */
    @Indexed
    private String uscc;

    /**
     * 营业执照
     */
    private String businessLicense;
    /**
     * 其他资料:如开户行许可证，食品安全许可证
     */
    private String otherSupportPhotos;

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
