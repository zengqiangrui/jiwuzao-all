package com.jiwuzao.common.dto.express;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
/**
 * 轨迹订阅接口请求数据
 */
public class ExpressRequestDataDto {
    @JsonProperty("ShipperCode")
    private String shipperCode;//快递公司编码
    @JsonProperty("LogisticCode")
    private String logisticCode;//快递单号
    @JsonProperty("PayType")
    private String payType = "1";//支付类型
    @JsonProperty("ExpType")
    private String expType;//快递类型


    @JsonProperty("Receiver")
    private ExpressUserDto receiver;//收件人信息
    @JsonProperty("Sender")
    private ExpressUserDto sender;//寄件人信息
}
