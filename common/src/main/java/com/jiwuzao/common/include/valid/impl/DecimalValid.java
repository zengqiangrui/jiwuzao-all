package com.jiwuzao.common.include.valid.impl;


import com.jiwuzao.common.include.Pattern;
import com.jiwuzao.common.include.valid.Decimal;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-02-24 12:30
 */
public class DecimalValid implements ConstraintValidator<Decimal,Object> {
    private boolean require = true;
    private boolean zero = false;

    @Override
    public void initialize(Decimal constraintAnnotation) {
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
            if(Pattern.isDecimal(String.valueOf(o),zero)){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
