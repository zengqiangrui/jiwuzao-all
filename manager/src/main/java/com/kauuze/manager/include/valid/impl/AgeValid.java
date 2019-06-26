package com.kauuze.manager.include.valid.impl;


import com.kauuze.manager.include.Pattern;
import com.kauuze.manager.include.valid.Age;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-02-24 12:30
 */
public class AgeValid implements ConstraintValidator<Age,Object> {
    private boolean require = true;

    @Override
    public void initialize(Age constraintAnnotation) {
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
            if(Pattern.isAge(Integer.valueOf(String.valueOf(o)))){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
