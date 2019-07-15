package com.jiwuzao.common.include.valid.impl;


import com.jiwuzao.common.include.Pattern;
import com.jiwuzao.common.include.valid.Password;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-02-24 12:30
 */
public class PasswordValid implements ConstraintValidator<Password,Object> {
    private boolean require = true;

    @Override
    public void initialize(Password constraintAnnotation) {
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
            if(Pattern.isPassword(String.valueOf(o))){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
