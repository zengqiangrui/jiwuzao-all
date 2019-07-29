package com.kauuze.major.config.contain.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "kdniao")
public class KdniaoProperties {
    private String eBusinessID;
    private String appKey;
    private String traceUrl;
    private String subscriptionUrl;

}
