package com.kauuze.manager.include.valid.impl;


import com.kauuze.manager.include.Pattern;
import com.kauuze.manager.include.valid.Url;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-02-24 12:30
 */
public class UrlValid implements ConstraintValidator<Url,Object> {
    private boolean require = true;

    @Override
    public void initialize(Url constraintAnnotation) {
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
            if(Pattern.isUrl(String.valueOf(o))){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
