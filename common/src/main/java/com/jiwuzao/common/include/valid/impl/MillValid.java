package com.jiwuzao.common.include.valid.impl;

import com.jiwuzao.common.include.Pattern;
import com.jiwuzao.common.include.valid.Mill;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-26 23:37
 */
public class MillValid implements ConstraintValidator<Mill,Object> {
    private boolean require = true;

    @Override
    public void initialize(Mill constraintAnnotation) {
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
            if(Pattern.isMill(Long.valueOf(String.valueOf(o)))){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
