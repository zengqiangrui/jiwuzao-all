package com.jiwuzao.common.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jiwuzao.common.domain.enumType.ExpressUserEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ExpressUserDto {
    private ExpressUserEnum status;//身份，寄件人或收件人

    @JsonProperty("Name")
    private String name;//姓名

    @JsonProperty("Tel")
    private String tel;//电话
    @JsonProperty("Mobile")
    private String mobile;//手机
    @JsonProperty("PostCode")
    private String postCode;//邮编EMS，YZPY，YZBK 必填
    @JsonProperty("ProvinceName")
    private String provinceName;//省

    @JsonProperty("CityName")
    private String cityName;//市

    @JsonProperty("ExpAreaName")
    private String expAreaName;//区

    @JsonProperty("Address")
    private String address;//详细地址

}
