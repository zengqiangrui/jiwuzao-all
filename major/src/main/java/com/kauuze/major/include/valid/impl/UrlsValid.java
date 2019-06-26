package com.kauuze.major.include.valid.impl;

import com.kauuze.major.include.Pattern;
import com.kauuze.major.include.valid.Urls;

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
            if(Pattern.isUrls(String.valueOf(o))){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
