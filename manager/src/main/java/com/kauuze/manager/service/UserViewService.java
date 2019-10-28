package com.kauuze.manager.service;


import com.jiwuzao.common.domain.enumType.RoleEnum;
import com.jiwuzao.common.domain.mongo.entity.userBastic.UserInfo;
import com.jiwuzao.common.domain.mongo.entity.userBastic.UserToken;
import com.jiwuzao.common.domain.mysql.entity.User;
import com.jiwuzao.common.include.PageDto;
import com.jiwuzao.common.include.PageUtil;
import com.jiwuzao.common.include.StringUtil;
import com.kauuze.manager.domain.common.MongoUtil;
import com.kauuze.manager.domain.mongo.repository.UserInfoRepository;
import com.kauuze.manager.domain.mongo.repository.UserTokenRepository;
import com.kauuze.manager.domain.mongo.repository.VerifyActorRepository;
import com.kauuze.manager.domain.mysql.repository.UserRepository;
import com.kauuze.manager.include.TokenUtil;
import com.kauuze.manager.service.dto.userView.UserShowDto;
import com.kauuze.manager.service.dto.userView.UserSimpleOpenDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-30 14:38
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class UserViewService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private UserTokenRepository userTokenRepository;
    @Autowired
    private VerifyActorRepository verifyActorRepository;

    /**
     * 通过uid查询，并清除冗余数据
     *
     * @param uid
     * @return
     */
    public UserShowDto findByUid(int uid) {
        //清除冗余开始
        UserToken userTokenTemp = userTokenRepository.findByUid(uid);
        if (userTokenTemp == null) {
            return null;
        }
        UserInfo userInfoTemp = userInfoRepository.findByUid(uid);
        if (userInfoTemp == null) {
            userTokenRepository.deleteById(userTokenTemp.getId());
            return null;
        }
        User user = userRepository.findById(uid);
        if (user == null) {
            userTokenRepository.deleteById(userTokenTemp.getId());
            userInfoRepository.deleteById(userInfoTemp.getId());
            return null;
        }
        UserToken userTokenUp = new UserToken();
        userTokenUp.setUid(uid);
        userTokenUp.setVip(user.getVip());
        if (user.getVipEndTime() == null) {
            userTokenUp.setVipEndTime(0L);
        } else {
            userTokenUp.setVipEndTime(user.getVipEndTime());
        }
        UserInfo userInfoUp = new UserInfo();
        userInfoUp.setUid(uid);
        userInfoUp.setNickName(user.getNickName());
        MongoUtil.updateNotNon("uid", userTokenUp, UserToken.class);
        MongoUtil.updateNotNon("uid", userInfoUp, UserInfo.class);
        //清除冗余结束
        UserInfo userInfo = userInfoRepository.findByUid(uid);
        UserToken userToken = userTokenRepository.findByUid(uid);
        userToken = TokenUtil.judgeEndTime(userToken);
        UserShowDto userShowDto = new UserShowDto(uid, user.getPhone(), userToken.getRole(), userToken.getBackRole(), userToken.getVip(), userToken.getVipEndTime(),
                userToken.getUserState(), userToken.getUserStateEndTime(), userInfo.getCreateTime(), userInfo.getNickName(), userInfo.getPortrait(), userInfo.getSex(),
                userInfo.getBirthday(), userInfo.getProvince(), userInfo.getCity(), userInfo.getPersonalSign(),
                userInfo.getOpenWxId(), userInfo.getOpenQQ());
        return userShowDto;
    }

    /**
     * 通过昵称查询
     *
     * @param
     * @return
     */
    public PageDto<UserSimpleOpenDto> findByNickName(String nickName, int num, int size) {
        Pageable pageable = PageUtil.getPageable(num, size);
        Page<UserInfo> page = userInfoRepository.findByNickNameLike(nickName, pageable);
        if (page.getSize() == 0) {
            return null;
        }
        List<UserInfo> list = page.getContent();
        List<UserSimpleOpenDto> usd = new ArrayList<>();
        for (UserInfo user : list) {
            User u = userRepository.findById(user.getUid().intValue());
            usd.add(new UserSimpleOpenDto(user.getUid(), user.getNickName(), user.getPortrait(), u.getPhone()));
        }
        return new PageDto<>(page.getTotalElements(), usd);
    }

    /**
     * 通过手机查询
     *
     * @param phone
     * @return
     */
    public UserShowDto findByPhone(String phone) {
        User user = userRepository.findByPhone(phone);
        if (user == null) {
            return null;
        }
        return findByUid(user.getId());
    }

    /**
     * 查询所有内容管理员信息
     *
     * @return
     */
    public PageDto<UserShowDto> findAllCms(int num, int size) {
        Pageable pageable = PageUtil.getPageable(num, size);
        Page<UserToken> userTokens = userTokenRepository.findCms(pageable);
        List<UserShowDto> list = new ArrayList<>();
        for (UserToken userToken : userTokens) {
            UserShowDto userShowDto = findByUid(userToken.getUid());
            if (userShowDto != null) {
                list.add(userShowDto);
            }
        }
        return new PageDto<>(userTokens.getTotalElements(), list);
    }

    /**
     * 违规昵称
     *
     * @param uid
     */
    public boolean illegalNickname(int uid) {
        User user = userRepository.findByIdForUpdate(uid);
        if (user == null) {
            return false;
        }
        if (TokenUtil.isBackRole(userTokenRepository.findByUid(uid))) {
            return false;
        }
        user.setNickName("违规" + StringUtil.hexBinDecOct(32, user.getId()));
        UserInfo userInfo = userInfoRepository.findByUid(uid);
        userInfo.setNickName("违规" + StringUtil.hexBinDecOct(32, user.getId()));
        userRepository.save(user);
        userInfoRepository.save(userInfo);
        return true;
    }

    /**
     * 违规头像
     *
     * @param uid
     */
    public boolean illegalPortrait(int uid) {
        User user = userRepository.findById(uid);
        if (user == null) {
            return false;
        }
        if (TokenUtil.isBackRole(userTokenRepository.findByUid(uid))) {
            return false;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("portrait", null);
        MongoUtil.updateMapStrictType("uid", uid, map, UserInfo.class);
        return true;
    }

    /**
     * 违规个性签名
     *
     * @param uid
     * @return
     */
    public boolean illegalPersonalSign(int uid) {
        User user = userRepository.findById(uid);
        if (user == null) {
            return false;
        }
        if (TokenUtil.isBackRole(userTokenRepository.findByUid(uid))) {
            return false;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("personalSign", "内容违规已删除");
        MongoUtil.updateMapStrictType("uid", uid, map, UserInfo.class);
        return true;
    }

    /**
     * 违规公开微信和QQ号
     *
     * @return
     */
    public boolean illegalOpenWxIdAndQQ(int uid) {
        User user = userRepository.findById(uid);
        if (user == null) {
            return false;
        }
        if (TokenUtil.isBackRole(userTokenRepository.findByUid(uid))) {
            return false;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("openWxId", null);
        map.put("openQQ", null);
        MongoUtil.updateMapStrictType("uid", uid, map, UserInfo.class);
        return true;
    }

    public PageDto<UserShowDto> findAllUser(Integer num, Integer size) {
        Page<UserToken> page = userTokenRepository.findAll(PageUtil.getNewsInsert(num, size));
        return new PageDto<UserShowDto>().setContent(page.map(userToken -> findByUid(userToken.getUid())).getContent()).setTotal(page.getTotalElements());
    }

    public Object findAllMerchant(Integer num, Integer size) {
        Page<UserToken> page = userTokenRepository.findAllByRole(RoleEnum.merchant, PageUtil.getNewsInsert(num, size));
        return new PageDto<UserShowDto>().setContent(page.map(userToken -> findByUid(userToken.getUid())).getContent()).setTotal(page.getTotalElements());
    }
}
