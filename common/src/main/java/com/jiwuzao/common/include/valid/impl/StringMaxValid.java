package com.jiwuzao.common.include.valid.impl;


import com.jiwuzao.common.include.Pattern;
import com.jiwuzao.common.include.StringUtil;
import com.jiwuzao.common.include.valid.StringMax;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-02-24 12:30
 */
public class StringMaxValid implements ConstraintValidator<StringMax,Object> {
    private boolean require = true;
    private int max;

    @Override
    public void initialize(StringMax constraintAnnotation) {
        if(!constraintAnnotation.require()){
            require = false;
        }
        max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        try {
            if(Pattern.require(o,require)){
                return true;
            }
            if(StringUtil.isBlank(String.valueOf(o))){
                return false;
            }
            if(String.valueOf(o).length() <= max){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
