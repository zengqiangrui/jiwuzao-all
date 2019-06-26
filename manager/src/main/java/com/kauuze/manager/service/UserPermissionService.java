package com.kauuze.manager.service;


import com.kauuze.manager.domain.common.MongoUtil;
import com.kauuze.manager.domain.enumType.BackRoleEnum;
import com.kauuze.manager.domain.enumType.UserStateEnum;
import com.kauuze.manager.domain.mongo.entity.userBastic.UserToken;
import com.kauuze.manager.domain.mongo.repository.UserTokenRepository;
import com.kauuze.manager.domain.mysql.entity.User;
import com.kauuze.manager.domain.mysql.repository.UserRepository;
import com.kauuze.manager.include.TokenUtil;
import com.kauuze.manager.service.dto.userView.UserShowDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-29 17:56
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class UserPermissionService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserTokenRepository userTokenRepository;
    @Autowired
    private UserViewService userViewService;

    /**
     * 封禁用户
     * @param uid
     * @param userState
     * @param userStateEndTime
     */
    public String forbid(int uid,UserStateEnum userState,long userStateEndTime){
        UserShowDto userShowDto = userViewService.findByUid(uid);
        if(userShowDto == null){
            return "操作失败";
        }
        UserToken userToken = userTokenRepository.findByUid(uid);
        if (TokenUtil.isBackRole(userToken)){
            return "无法封禁管理员";
        }
        if(TokenUtil.isMerchant(userToken)){
            return "无法封禁商家";
        }
        UserToken userTokenUp = new UserToken();
        userTokenUp.setId(userToken.getId());
        userTokenUp.setAccessToken(TokenUtil.generateToken(uid));
        userTokenUp.setUserState(userState);
        userTokenUp.setUserStateEndTime(userStateEndTime);
        MongoUtil.updateNotNon("id",userTokenUp,UserToken.class);
        return null;
    }

    /**
     * 解除封禁
     */
    public boolean relieve(int uid){
        UserShowDto userShowDto = userViewService.findByUid(uid);
        if(userShowDto == null){
            return false;
        }
        UserToken userToken = userTokenRepository.findByUid(uid);
        UserToken userTokenUp = new UserToken();
        userTokenUp.setId(userToken.getId());
        userTokenUp.setAccessToken(TokenUtil.generateToken(uid));
        userTokenUp.setUserState(UserStateEnum.normal);
        userTokenUp.setUserStateEndTime(0L);
        MongoUtil.updateNotNon("id",userTokenUp,UserToken.class);
        return true;
    }

    /**
     * 重置密码
     */
    public boolean resetPwd(int uid){
        UserShowDto userShowDto = userViewService.findByUid(uid);
        if(userShowDto == null){
            return false;
        }
        UserToken userToken = userTokenRepository.findByUid(uid);
        if (TokenUtil.isBackRole(userToken)){
            return false;
        }
        User user = userRepository.findByIdForUpdate(uid);
        user.setPwdFailCount(20);
        userRepository.save(user);
        return true;
    }

    /**
     * 任命内容管理员
     * @param uid
     */
    public boolean appointCms(int uid){
        relieve(uid);
        UserShowDto userShowDto = userViewService.findByUid(uid);
        if(userShowDto == null){
            return false;
        }
        UserToken userToken = userTokenRepository.findByUid(uid);
        if (TokenUtil.isBackRole(userToken)){
            return false;
        }
        UserToken userTokenUp = new UserToken();
        userTokenUp.setId(userToken.getId());
        userTokenUp.setBackRole(BackRoleEnum.cms);
        MongoUtil.updateNotNon("id",userTokenUp,UserToken.class);
        return true;
    }

    /**
     * 取消内容管理员
     */
    public boolean cancelCms(int uid){
        UserShowDto userShowDto = userViewService.findByUid(uid);
        if(userShowDto == null){
            return false;
        }
        UserToken userToken = userTokenRepository.findByUid(uid);
        if (!TokenUtil.isCms(userToken)){
            return false;
        }
        if(TokenUtil.isRoot(userToken)){
            return false;
        }
        UserToken userTokenUp = new UserToken();
        userTokenUp.setId(userToken.getId());
        userTokenUp.setBackRole(BackRoleEnum.user);
        MongoUtil.updateNotNon("id",userTokenUp,UserToken.class);
        return true;
    }


}
