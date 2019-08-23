package com.kauuze.major;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-23 11:15
 */
@Component
public class ConfigUtil {
    /**
     * 注解需要常量，这里手动更改
     */
    public static final String customEnvironment = "dev";
    /**
     * 运行配置环境
     */
    private static String customEnvironmentSet;
    @Value("${custom.environment}")
    public void setCustomEnvironment(String customEnvironment){
        customEnvironmentSet = customEnvironment;
    }
    public static boolean validCustomEnvironment(){
        return customEnvironment.equals(customEnvironmentSet);
    }

}
