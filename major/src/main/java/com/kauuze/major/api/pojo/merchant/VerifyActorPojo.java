package com.kauuze.major.api.pojo.merchant;

import com.kauuze.major.domain.enumType.OpeningBankEnum;
import com.kauuze.major.include.valid.*;
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
    private String frontIdCardPhoto;
    @Url
    private String handIdCardPhoto;
    @Url
    private String backIdCardPhoto;
    @BankNo
    private Long bankNo;
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
