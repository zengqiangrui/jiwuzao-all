package com.kauuze.major.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.kauuze.major.config.contain.properties.WxPayProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-02-24 12:30
 */
@Configuration
public class ConfigBean implements WebMvcConfigurer {
    @Autowired
    RestTemplateBuilder restTemplateBuilder;
    @Autowired
    private WxPayProperties wxPayProperties;

    @Bean
    public RestTemplate restTemplate(){
        return restTemplateBuilder.build();
    }

    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }

    @Bean
    public WxPayService wxPayService() {
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(StringUtils.trimToNull(wxPayProperties.getAppId()));
        payConfig.setMchId(StringUtils.trimToNull(wxPayProperties.getMchId()));
        payConfig.setMchKey(StringUtils.trimToNull(wxPayProperties.getMchKey()));
        payConfig.setKeyPath(StringUtils.trimToNull(wxPayProperties.getKeyPath()));
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }



}
