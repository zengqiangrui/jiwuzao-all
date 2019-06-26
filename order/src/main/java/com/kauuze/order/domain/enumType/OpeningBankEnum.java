package com.kauuze.order.domain.enumType;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-29 20:35
 */
public enum OpeningBankEnum {
    /**
     * 工商银行
     */
    ICBC(1002),
    /**
     * 农业银行
     */
    ABC(1005),
    /**
     * 中国银行
     */
    BOC(1026),
    /**
     * 建设银行
     */
    CCB(1003),
    /**
     * 招商银行
     */
    CMB(1001),
    /**
     * 邮储银行
     */
    PSBC(1066),
    /**
     * 交通银行
     */
    BCM(1020),
    /**
     * 浦发银行
     */
    SPDB(1004),
    /**
     * 民生银行
     */
    CMBC(1006),
    /**
     * 兴业银行
     */
    CIB(1009),
    /**
     * 平安银行
     */
    PABC(1010),
    /**
     * 中信银行
     */
    CNCB(1021),
    /**
     * 华夏银行
     */
    HXB(1025),
    /**
     * 广发银行
     */
    CGB(1027),
    /**
     * 光大银行
     */
    CEB(1022),
    /**
     * 北京银行
     */
    BOB(4836),
    /**
     * 宁波银行
     */
    NBCB(1056),
    /**
     * 上海银行
     */
    BOSC(1024);

    public Integer wxCode;
    private OpeningBankEnum(Integer wxCode){
        this.wxCode = wxCode;
    }
}
