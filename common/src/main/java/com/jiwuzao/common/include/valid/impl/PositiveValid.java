package com.jiwuzao.common.include.valid.impl;


import com.jiwuzao.common.include.Pattern;
import com.jiwuzao.common.include.valid.Positive;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-03-11 23:15
 */
public class PositiveValid implements ConstraintValidator<Positive,Object> {
    private boolean require = true;
    private boolean zero = false;
    @Override
    public void initialize(Positive constraintAnnotation) {
        if(!constraintAnnotation.require()){
            require = false;
        }
        if(constraintAnnotation.zero()){
            zero = true;
        }
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        try {
            if(Pattern.require(o,require)){
                return true;
            }
            if(Pattern.isPositive(String.valueOf(o),zero)){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
