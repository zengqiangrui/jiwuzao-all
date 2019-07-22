package com.kauuze.manager.include;


import com.jiwuzao.common.config.contain.SpringContext;
import com.jiwuzao.common.domain.enumType.BackRoleEnum;
import com.jiwuzao.common.domain.enumType.RoleEnum;
import com.jiwuzao.common.domain.enumType.UserStateEnum;
import com.jiwuzao.common.domain.mongo.entity.userBastic.UserInfo;
import com.jiwuzao.common.domain.mongo.entity.userBastic.UserToken;
import com.jiwuzao.common.domain.mysql.entity.User;
import com.kauuze.manager.domain.common.MongoUtil;
import com.kauuze.manager.domain.mongo.repository.UserTokenRepository;
import com.kauuze.manager.domain.mysql.repository.UserRepository;

import java.util.List;


/**
 * 权限验证工具
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-15 02:00
 */
public class TokenUtil {

    /**
     * 刷新mysql在mongodb中的冗余字段,并刷新socketToken
     * @return
     */
    public static void refreshRddy(int uid){
        UserRepository userRepository = SpringContext.getBean(UserRepository.class);
        User user = userRepository.findById(uid);
        if(user == null){
            return;
        }else{
            UserToken userToken = new UserToken();
            userToken.setUid(uid);
            userToken.setVip(user.getVip());
            if(user.getVipEndTime() == null){
                userToken.setVipEndTime(0L);
            }else{
                userToken.setVipEndTime(user.getVipEndTime());
            }
            userToken.setAccessToken(TokenUtil.generateToken(uid));
            UserInfo userInfo = new UserInfo();
            userInfo.setUid(uid);
            userInfo.setNickName(user.getNickName());
            MongoUtil.updateNotNon("uid",userToken,UserToken.class);
            MongoUtil.updateNotNon("uid",userInfo,UserInfo.class);
        }
    }

    public static UserToken judgeEndTime(UserToken userToken){
        if(userToken.getVip() && System.currentTimeMillis() > userToken.getVipEndTime()){
            userToken.setVip(false);
        }
        if(userToken.getUserState() != UserStateEnum.normal &&  System.currentTimeMillis() > userToken.getUserStateEndTime()){
            userToken.setUserState(UserStateEnum.normal);
        }
        return userToken;
    }

    /**
     * 生成令牌
     * @param uid
     * @return
     */
    public static String generateToken(int uid){
        return uid + "," + Rand.getUUID() + Rand.getStringMax(32);
    }

    /**
     * 验证access令牌，null为访问令牌不合法需重新登录
     * @param accessToken
     * @return
     */
    public static UserToken validAccessToken(String accessToken){
        if(StringUtil.isBlank(accessToken)){
            return null;
        }
        List<String> list = StringUtil.splitComma(accessToken);
        if(list.size() != 2){
            return null;
        }else {
            UserTokenRepository userTokenRepository = SpringContext.getBean(UserTokenRepository.class);
            UserToken userToken = userTokenRepository.findByUid(Integer.valueOf(list.get(0)));
            if(userToken != null && StringUtil.isEq(userToken.getAccessToken(),accessToken)){
                return userToken;
            }else {
                return null;
            }
        }
    }

    /**
     * 是否为Vip用户
     * @param userToken
     * @return
     */
    public static Boolean isVip(UserToken userToken){
        if(!userToken.getVip()){
            return false;
        }
        if(userToken.getVipEndTime() >= System.currentTimeMillis()){
            return true;
        }
        return false;
    }

    /**
     * 是否为后台用户
     * @param userToken
     * @return
     */
    public static Boolean isBackRole(UserToken userToken){
        if(BackRoleEnum.user != userToken.getBackRole()){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 是否为系统管理员用户
     * @param userToken
     * @return
     */
    public static Boolean isRoot(UserToken userToken){
        if (BackRoleEnum.root == userToken.getBackRole()){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 是否为内容管理员用户
     * @param userToken
     * @return
     */
    public static Boolean isCms(UserToken userToken){
        if (isRoot(userToken)){
            return true;
        }
        if(BackRoleEnum.cms == userToken.getBackRole()){
            return true;
        }
        return false;
    }


    /**
     * 是否禁止发言
     * @param userToken
     * @return
     */
    public static Boolean isShutup(UserToken userToken){
        if(isBackRole(userToken)){
            return false;
        }
        if(UserStateEnum.shutup != userToken.getUserState()){
            return false;
        }
        if(userToken.getUserStateEndTime() <= System.currentTimeMillis()){
            return false;
        }
        return true;
    }

    /**
     * 是否被封禁
     * @param userToken
     * @return
     */
    public static Boolean isBan(UserToken userToken){
        if(isBackRole(userToken)){
            return false;
        }
        if(UserStateEnum.ban != userToken.getUserState()){
            return false;
        }
        if(userToken.getUserStateEndTime() <= System.currentTimeMillis() ){
            return false;
        }
        return true;
    }


    /**
     * 是否是商家角色
     * @param userToken
     * @return
     */
    public static Boolean isMerchant(UserToken userToken){
        RoleEnum roleEnum = userToken.getRole();
        if(roleEnum == RoleEnum.merchant){
            return true;
        }
        return false;
    }
}
