package com.kauuze.major.config.contain.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "tencent.sms")
public class TencentSmsPropertis {
    private Integer appId;
    private String appKey;
    private Integer smsTemplateId;
    private Integer deliverTemplateId;
}
