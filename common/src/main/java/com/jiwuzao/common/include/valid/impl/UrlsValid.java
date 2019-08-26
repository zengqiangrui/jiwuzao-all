package com.jiwuzao.common.include.valid.impl;

import com.jiwuzao.common.include.Pattern;
import com.jiwuzao.common.include.valid.Urls;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-27 21:08
 */
public class UrlsValid implements ConstraintValidator<Urls,Object> {
    private boolean require = true;

    @Override
    public void initialize(Urls constraintAnnotation) {
        if(!constraintAnnotation.require()){
            require = false;
        }
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        try {
            if(Pattern.require(o,require)){
                return true;
            }
            return Pattern.isUrls(String.valueOf(o));
        } catch (Exception e) {
            return false;
        }
    }
}
