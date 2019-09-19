package com.kauuze.major.config.contain;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaInMemoryConfig;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.kauuze.major.config.contain.properties.WxMaProperties;
import com.kauuze.major.config.contain.properties.WxPayProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class WxMaConfiguration {
  @Resource
  private WxMaProperties properties;

  @Bean
  public WxMaService wxMaServiceConfig() {
    WxMaInMemoryConfig wxMaInMemoryConfig = new WxMaInMemoryConfig();
    wxMaInMemoryConfig.setAppid(properties.getAppId());
    wxMaInMemoryConfig.setSecret(properties.getAppSecret());
    WxMaService service = new WxMaServiceImpl();
    service.setWxMaConfig(wxMaInMemoryConfig);
    return service;
  }


}