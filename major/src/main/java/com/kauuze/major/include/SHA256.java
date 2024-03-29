package com.kauuze.major.include;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 用于密码加密
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-02-24 12:30
 */
public class SHA256 {

    public static String generateSalt(){
        return Rand.getUUID() + Rand.getStringMax(32);
    }

    public static String encryptAddSalt(String str,String salt){
        return encrypt(str + salt);
    }

    private static String encrypt(String str){
        MessageDigest messageDigest;
        String encodeStr = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    private static String byte2Hex(byte[] bytes){
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i=0;i<bytes.length;i++){
        temp = Integer.toHexString(bytes[i] & 0xFF);
        if (temp.length()==1){
            //1得到一位的进行补0操作
            stringBuffer.append("0");
        }
            stringBuffer.append(temp);
        }
            return stringBuffer.toString();
        }

}