package com.kauuze.manager.include.valid.impl;


import com.kauuze.manager.include.Pattern;
import com.kauuze.manager.include.valid.OrderNo;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-23 16:35
 */
public class OrderNoValid implements ConstraintValidator<OrderNo,Object> {
    private boolean require = true;

    @Override
    public void initialize(OrderNo constraintAnnotation) {
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
            if(Pattern.isOrderNo(String.valueOf(o))){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
