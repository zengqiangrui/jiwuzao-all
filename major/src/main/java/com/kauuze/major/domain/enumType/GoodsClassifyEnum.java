package com.kauuze.major.domain.enumType;

import lombok.Getter;

@Getter
public enum GoodsClassifyEnum {
    recommend(0,"推荐"),
    /**
     * 灵物
     */
    special(1,"灵物"),
    /**
     * 食品
     */
    food(2,"食品"),
    /**
     * 服装
     */
    clothing(3,"服装"),
    /**
     * 饰品
     */
    jewelry(4,"饰品"),
    /**
     * 箱包
     */
    bags(5,"箱包"),
    /**
     * 器具
     */
    appliance(6,"器具"),

    /**
     * 极礼
     */
    gift(7,"极礼"),

    /**
     * 美业
     */
    beauty(8,"美业"),

    /**
     * 家居
     */
    house(9,"家居"),
    /**
     * 母婴
     */
    mom(10,"母婴"),
    /**
     * 玩具
     */
    toy(11,"玩具"),
    /**
     * 器具
     */
    tool(12,"工具"),
    /**
     * 电子
     */
    electron(13,"电子"),
    /**
     * 其他
     */
    other(14,"其他");

    private Integer code;

    private String msg;

    GoodsClassifyEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
