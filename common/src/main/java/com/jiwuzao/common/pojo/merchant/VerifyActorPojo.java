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
    @BankNo
    private Long bankNo;//银行号码
    @TrueName
    private String bankTrueName;
    @NotNull
    private OpeningBankEnum openingBank;
    @StringMax(max = 30)
    private String companyName;
    @StringMax(max = 18)
    private String uscc;
    @Url
    private String businessLicense;
    @Urls
    private String otherSupportPhotos;
}
