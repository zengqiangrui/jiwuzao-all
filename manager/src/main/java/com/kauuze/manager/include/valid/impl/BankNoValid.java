package com.kauuze.manager.include.valid.impl;

import com.kauuze.manager.include.Pattern;
import com.kauuze.manager.include.valid.BankNo;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-06-03 13:47
 */
public class BankNoValid implements ConstraintValidator<BankNo,Object> {
    private boolean require = true;
    @Override
    public void initialize(BankNo constraintAnnotation) {
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
            if(Pattern.isBankNo(Long.valueOf(String.valueOf(o)))){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
