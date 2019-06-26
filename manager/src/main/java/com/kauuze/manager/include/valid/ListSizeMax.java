package com.kauuze.manager.include.valid;


import com.kauuze.manager.include.valid.impl.ListSizeMaxValid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-02-24 12:30
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ListSizeMaxValid.class)
public @interface ListSizeMax {
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    boolean require() default true;
    int max() default 50;
}
