package com.kauuze.major.config.permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 需要商家角色:依靠平台盈利的人
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-20 20:14
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Merchant {
}
