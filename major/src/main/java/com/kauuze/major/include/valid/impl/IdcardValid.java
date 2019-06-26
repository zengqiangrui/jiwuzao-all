package com.kauuze.major.include.valid.impl;


import com.kauuze.major.include.Pattern;
import com.kauuze.major.include.valid.Idcard;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-02-24 12:30
 */
public class IdcardValid implements ConstraintValidator<Idcard,Object> {
    private boolean require = true;

    @Override
    public void initialize(Idcard constraintAnnotation) {
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
            if(Pattern.isIdcard(String.valueOf(o))){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
