package com.kauuze.major.include;


import com.jiwuzao.common.pojo.common.PagePojo;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;

/**
 * 用于数据效验，参数为null通常为false
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-02-24 12:30
 */
public class Pattern {

    /**
     * 信息类型:html,image(jpg,jpeg,png,gif),audio(mp3),video(mp4)
     * html要求前端插入时做filterXSS验证,提交前trim
     * @param infoType
     * @return
     */
    public static boolean isInfoType(String infoType){
        return StringUtil.isEq(infoType, "html") || StringUtil.isEq(infoType, "image") || StringUtil.isEq(infoType, "audio") || StringUtil.isEq(infoType, "video");
    }


    /**
     * 是否是密码
     * @param password
     * @return
     */
    public static boolean isPassword(String password){
        if(patternReg(password, "^[a-zA-Z0-9^%&',;=$\\x22\\(\\)\\[\\]\\|\\{\\}\\+\\-\\*!@#_/<>\\.:\\?]{8,32}$")){
            if(!isPositive(password,false)){
                return true;
            }
        }
        return false;
    }


    /**
     * 是否是手机验证码
     * @param msCode
     * @return
     */
    public static boolean isMsCode(Integer msCode){
        return patternReg(String.valueOf(msCode), "^[0-9]{6}$");
    }

    /**
     * 2-8字符,且不能为纯数字
     *汉字字母数字下划线短横线
     * @param nickeName
     * @return
     */
    public static boolean isNickName(String nickeName){
        if(StringUtil.isBlank(nickeName)){
            return false;
        }
        if(isPositive(nickeName,false)){
            return false;
        }
        if(patternReg(nickeName,"^[a-zA-Z0-9\\u4e00-\\u9fa5_-]{2,8}$")){
            return true;
        }else{
            return false;
        }
    }
    //以下是固定规则

    /**
     * 正整数
     * @return
     */
    public static boolean isPositive(String str,boolean zero){
        if(str == null || StringUtil.isEq(str,"null")){
            return false;
        }
        if(str.length() > 10){
            return false;
        }
        try {
            if(zero){
                if(Integer.valueOf(str) == 0){
                    return true;
                }
            }else{
                if(Integer.valueOf(str) == 0){
                    return false;
                }
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return patternReg(str,"^[0-9]*[1-9][0-9]*$");
    }

    /**
     * 正浮点数
     * @param str
     * @return
     */
    public static boolean isDecimal(String str,boolean zero){
        if(str == null || str == "null" || str.length() > 13){
            return false;
        }
        BigDecimal bigDecimal = null;
        try {
            bigDecimal = new BigDecimal(str);
        } catch (Exception e) {
            return false;
        }
        if(zero){
            if(new BigDecimal("0.01").compareTo(bigDecimal) > 0){
                return true;
            }
        }else{
            if(new BigDecimal("0.01").compareTo(bigDecimal) > 0){
                return false;
            }
        }
        BigDecimal bigDecimal2 = bigDecimal.setScale(2,BigDecimal.ROUND_HALF_DOWN);
        if(bigDecimal2.compareTo(bigDecimal) != 0){
            return false;
        }
        return patternReg(str,"^(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))$");
    }
    /**
     * 银行卡
     * @return
     */
    public static boolean isBankNo(Long bankNo){
       return patternReg(String.valueOf(bankNo),"^[6][0-9]{18}$");
    }

    /**
     * 1971--2171年
     * @param mill
     * @return
     */
    public static boolean isMill(Long mill){
        if(mill == null){
            return false;
        }
        if(mill >= 31507200000L && mill <= 6342940800000L){
            return true;
        }
        return false;
    }

    /**
     * list不为空,不超过最大值返回true
     * @param list
     * @param max
     * @return
     */
    public static boolean listSizeMax(List list, int max){
        if(list == null){
            return false;
        }
        if(JsonUtil.toJsonString(list).length() > 10000){
            return false;
        }
        int size = list.size();
        if (size > max || size == 0){
            return false;
        }
        return true;
    }

    /**
     * 年龄
     * @param age
     * @return
     */
    public static boolean isAge(Integer age){
        return patternReg(String.valueOf(age),"^((1[01][0-9])|(120)|([1-9][0-9])|([0-9]))$");
    }

    /**
     * 手机号
     * @param phone
     * @return
     */
    public static boolean isPhone(String phone){
        if(!patternReg(phone,"^[1][0-9]{10}$")){
            return false;
        }
        return true;
    }

    /**
     * 身份证
     * @param idCard
     * @return
     */
    public static boolean isIdcard(String idCard){
        return patternReg(idCard,"^[1-9]\\d{5}(19|20)\\d{2}((0[1-9])|10|11|12)(([0-2][1-9])|10|20|30|31)\\d{3}[0-9X]$");
    }

    /**
     * 订单编号
     * @param orderNo
     * @return
     */
    public static boolean isOrderNo(String orderNo){
        if(patternReg(orderNo,"^[a-zA-Z0-9]{8,32}$")){
            return true;
        }
        return false;
    }

    /**
     * 验证分页参数
     * @param pagePojo
     * @return
     */
    public static boolean validPage(PagePojo pagePojo){
        if(isPositive(String.valueOf(pagePojo.getNum()),false) && isPositive(String.valueOf(pagePojo.getSize()),false)
                && isMill(pagePojo.getTime())){
            return true;
        }
        return false;
    }

    /**
     * 数据库id字段
     * @param id
     * @return
     */
    public static boolean isId(String id){
        if(id == null || StringUtil.isEq(id,"null")){
            return false;
        }
        if(patternReg(id,"[a-zA-Z0-9]{1,32}")){
            return true;
        }
        return false;
    }

    public static boolean isUrl(String url){
        if(patternReg(url,"^((ht|f)tps?):\\/\\/[\\w\\-]+(\\.[\\w\\-]+)+([\\w\\-.,@?^=%&:\\/~+#]*[\\w\\-@?^=%&\\/~+#])?$")){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isUrls(String urls){
        List<String> list = StringUtil.splitComma(urls);
        for (String s : list) {
            if(!isUrl(s)){
                return false;
            }
        }
        if(list.size() > 9){
            return false;
        }
        return true;
    }

    /**
     * 是否是IP地址
     * @param ipAddress
     * @return
     */
    public static boolean isIpAddress(String ipAddress){
        return patternReg(ipAddress,"(2(5[0-5]{1}|[0-4]\\d{1})|[0-1]?\\d{1,2})(\\.(2(5[0-5]{1}|[0-4]\\d{1})|[0-1]?\\d{1,2})){3}");
    }
    /**
     *真实姓名
     * @param trueName
     * @return
     */
    public static boolean isTrueName(String trueName){
        return patternReg(trueName, "^[\\u4e00-\\u9fa5]{2,15}$");
    }

    /**
     * QQ号:5-15位数字
     * @return
     */
    public static boolean isQQ(String qq){
        return patternReg(qq,"^[1-9][0-9]{4,14}$");
    }

    /**
     * 微信号
     * @return
     */
    public static boolean isWxId(String wxId){
        return patternReg(wxId,"^[a-zA-Z]([-_a-zA-Z0-9]{5,19})+$");
    }
    /**
     * 处理空值是否通过验证
     * @param o
     * @param require
     * @return
     */
    public static boolean require(Object o, boolean require){
        if(!require){
            if(o == null){
                return true;
            }
            try {
                if(StringUtil.isBlank(String.valueOf(o))){
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    /**
     * 字符串为空返回false
     * @param str
     * @param reg
     * @return
     */
    private static boolean patternReg(String str,String reg){
        if(StringUtil.isBlank(str) || StringUtil.isBlank(reg)){
            return false;
        }
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(reg);
        Matcher matcher = pattern.matcher(str);
        boolean rs = matcher.matches();
        return rs;
    }
}
