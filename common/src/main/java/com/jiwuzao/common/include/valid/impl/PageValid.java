package com.jiwuzao.common.include.valid.impl;

import com.jiwuzao.common.include.JsonUtil;
import com.jiwuzao.common.include.Pattern;
import com.jiwuzao.common.include.valid.Page;
import com.jiwuzao.common.pojo.common.PagePojo;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-07 20:11
 */
public class PageValid implements ConstraintValidator<Page,Object> {
    private boolean require = true;
    @Override
    public void initialize(Page constraintAnnotation) {
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
            if(Pattern.validPage(JsonUtil.copy(o, PagePojo.class))){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
