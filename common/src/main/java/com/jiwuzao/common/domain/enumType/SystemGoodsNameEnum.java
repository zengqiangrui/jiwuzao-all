package com.jiwuzao.common.domain.enumType;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-22 10:59
 */
public enum SystemGoodsNameEnum {
    /**
     * 商家保证金
     */
    deposit("商家保证金");

    public String name;
    private SystemGoodsNameEnum(String name){
        this.name = name;
    }
}
