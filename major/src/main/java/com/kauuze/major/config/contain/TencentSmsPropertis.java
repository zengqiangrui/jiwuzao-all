package com.kauuze.major.config.contain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "tencent.sms")
public class TencentSmsPropertis {
    private Integer appId;
    private String appKey;
    private String sign;
    private Integer templateId;
}
