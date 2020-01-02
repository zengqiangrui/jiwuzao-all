package com.jiwuzao.common.pojo.merchant;

import com.jiwuzao.common.domain.enumType.OpeningBankEnum;
import com.jiwuzao.common.include.valid.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-27 22:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class VerifyActorPojo {
    @TrueName
    private String trueName;
    @Idcard
    private String idcard;
    @Url
    private String frontIdCardPhoto;//身份证正面
    @Url
    private String handIdCardPhoto;//手持身份证
    @Url
    private String backIdCardPhoto;//身份证背面
    @StringMax(max = 32)
    private String bankNo;//银行开户行
    @StringMax
    private String bankTrueName;//开户名称
    @StringMax
    private String openingBank;
    @StringMax
    private String companyName;//公司名
    @StringMax
    private String uscc;
    @Url
    private String businessLicense;
    @Url
    private String accountOpenLicence;
    @Urls
    private String otherSupportPhotos;
}