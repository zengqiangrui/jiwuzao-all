package com.kauuze.major.config.contain.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "wx.ma")
/**
 * 小程序相关
 */
public class WxMaProperties {
    private String appId;
    private String appSecret;
}
