package com.jiwuzao.common.include.valid.impl;

import com.jiwuzao.common.include.Pattern;
import com.jiwuzao.common.include.valid.IpAddress;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-22 15:43
 */
public class IpAddressValid implements ConstraintValidator<IpAddress,Object> {
    private boolean require = true;
    @Override
    public void initialize(IpAddress constraintAnnotation) {
        if(!constraintAnnotation.require()){
            require = false;
        }
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext context) {
        try {
            if(Pattern.require(o,require)){
                return true;
            }
            if(Pattern.isIpAddress(String.valueOf(o))){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
