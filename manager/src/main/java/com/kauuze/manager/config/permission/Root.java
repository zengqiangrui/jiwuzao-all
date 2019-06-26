package com.kauuze.manager.config.permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 需要系统管理员角色
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-06 21:49
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Root {
}