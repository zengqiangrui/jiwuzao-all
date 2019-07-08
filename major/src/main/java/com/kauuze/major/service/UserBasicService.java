package com.kauuze.major.service;

import com.kauuze.major.domain.common.MongoUtil;
import com.kauuze.major.domain.enumType.*;
import com.kauuze.major.domain.mongo.entity.userBastic.Store;
import com.kauuze.major.domain.mongo.entity.userBastic.UserInfo;
import com.kauuze.major.domain.mongo.entity.userBastic.UserToken;
import com.kauuze.major.domain.mongo.repository.StoreRepository;
import com.kauuze.major.domain.mongo.repository.UserInfoRepository;
import com.kauuze.major.domain.mongo.repository.UserTokenRepository;
import com.kauuze.major.domain.mysql.entity.Sms;
import com.kauuze.major.domain.mysql.entity.User;
import com.kauuze.major.domain.mysql.repository.SmsRepository;
import com.kauuze.major.domain.mysql.repository.UserRepository;
import com.kauuze.major.domain.mysql.repository.WithdrawOrderRepository;
import com.kauuze.major.include.*;
import com.kauuze.major.include.yun.QiniuUtil;
import com.kauuze.major.include.yun.SmsUtil;
import com.kauuze.major.service.dto.userBasic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-23 12:14
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class UserBasicService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SmsRepository smsRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private UserTokenRepository userTokenRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private WithdrawOrderRepository withdrawOrderRepository;

    /**
     * 发送短信验证码:有效期5分钟。null为发送失败
     * @param phone
     */
    public Integer sendSms(String phone){
        int msCode = Rand.getNumber(6);
        if(!SmsUtil.sendTp1(phone, msCode)){//发送失败
            return null;
        }
        Sms sms = smsRepository.findByPhone(phone);
        if(sms == null){
            smsRepository.save(new Sms(null,phone,msCode, DateTimeUtil.covertMill(LocalDateTime.now().plusMinutes(5)),0));
        }else{
            sms = smsRepository.findByIdForUpdate(sms.getId());
            sms.setOverTime(System.currentTimeMillis() + DateTimeUtil.getOneMinMill()*5);
            sms.setFailCount(0);
            sms.setCode(msCode);
            smsRepository.save(sms);
        }
        return msCode;
    }

    /**
     * 验证短信验证码
     * @param phone
     * @param msCode
     * @return
     */
    public boolean validSms(String phone,int msCode){
        Sms sms = smsRepository.findByPhone(phone);
        if(sms == null){
            return false;
        }
        if(sms.getCode() == msCode && sms.getOverTime() > System.currentTimeMillis() && sms.getFailCount() < 3){
            return true;
        }else{
            sms = smsRepository.findByIdForUpdate(sms.getId());
            sms.setFailCount(sms.getFailCount() + 1);
            smsRepository.save(sms);
            return false;
        }
    }

    /**
     * 每次APP开屏,刷新冗余数据和token，获取用户私有信息
     * @return
     */
    public UserPrivateDto getUserPrivateDto(int uid){
        User user = userRepository.findById(uid);
        if(user == null){
            return null;
        }
        TokenUtil.refreshRddy(uid);
        UserToken userToken = userTokenRepository.findByUid(uid);
        UserInfo userInfo = userInfoRepository.findByUid(uid);
        userToken = TokenUtil.judgeEndTime(userToken);
        UserPrivateDto userPrivateDto = new UserPrivateDto(uid,user.getPhone(),userToken.getRole(),userToken.getBackRole(),userToken.getVip(),userToken.getVipEndTime(),userToken.getAccessToken(),userToken.getUserState(),userToken.getUserStateEndTime(),System.currentTimeMillis(),userInfo.getNickName(),userInfo.getPortrait(),userInfo.getSex(),userInfo.getBirthday(),userInfo.getProvince(),userInfo.getCity(),userInfo.getPersonalSign(),userInfo.getOpenWxId(),userInfo.getOpenQQ());
        return userPrivateDto;
    }

    /**
     * 获取用户简单公开信息
     * @param uid
     * @return
     */
    public UserSimpleOpenDto getUserSimpleOpenDto(int uid){
        UserInfo userInfo = userInfoRepository.findByUid(uid);
        UserToken userToken = userTokenRepository.findByUid(uid);
        if(userInfo == null || userToken == null){
            return null;
        }
        userToken = TokenUtil.judgeEndTime(userToken);
        UserSimpleOpenDto userSimpleOpenDto = new UserSimpleOpenDto(uid,userToken.getVip(),userInfo.getNickName(),userInfo.getPortrait());
        return userSimpleOpenDto;
    }

    /**
     * 获取用户全部公开信息
     * @param uid
     * @return
     */
    public UserOpenDto getUserOpenDto(int uid){
        UserInfo userInfo = userInfoRepository.findByUid(uid);
        UserToken userToken = userTokenRepository.findByUid(uid);
        if(userInfo == null || userToken == null){
            return null;
        }
        userToken = TokenUtil.judgeEndTime(userToken);
        Store store = storeRepository.findByUid(uid);
        if(StringUtil.isBlank(store.getStoreName()) || store.getViolation()){
            store = new Store();
        }
        UserOpenDto userOpenDto = new UserOpenDto(uid,userInfo.getCreateTime(),userInfo.getNickName(),userInfo.getPortrait(),userInfo.getSex(),userInfo.getBirthday(),userInfo.getProvince(),userInfo.getCity(),userInfo.getPersonalSign(),userInfo.getOpenWxId(),userInfo.getOpenQQ(),userToken.getRole(),userToken.getVip(),userToken.getUserState(),userToken.getLastLoginTime(),userToken.getLastAccessTime(),store.getId());
        return userOpenDto;
    }

    /**
     * 获取用户简单公开信息列表
     * @param uids
     * @return
     */
    public List<UserSimpleOpenDto> getUserSimpleOpenDtos(List<Integer> uids){
        List<UserSimpleOpenDto> userSimpleOpenDtos = new ArrayList<>();
        for (Integer uid : uids) {
            userSimpleOpenDtos.add(getUserSimpleOpenDto(uid));
        }
        return userSimpleOpenDtos;
    }


    /**
     * 昵称查找用户
     * @param nickName
     * @param num
     * @param size
     * @return
     */
    public PageDto<UserSimpleOpenDto> searchByNickName(String nickName, int num, int size, Long time){
        Pageable pageable =  PageUtil.getPageable(num, size);
        Page<UserInfo> page2 = userInfoRepository.findByCreateTimeLessThanEqualAndNickNameLike(time,nickName,pageable);
        List<UserInfo> userInfos = page2.getContent();
        List<Integer> uids = new ArrayList<>();
        for (UserInfo userInfo : userInfos) {
            uids.add(userInfo.getUid());
        }
        List<UserSimpleOpenDto> userSimpleOpenDtos = getUserSimpleOpenDtos(uids);
        PageDto<UserSimpleOpenDto> page3 = new PageDto<>(page2.getTotalElements(),userSimpleOpenDtos);
        return page3;
    }

    /**
     * 店铺名称搜索商家
     * @param storeName
     * @param num
     * @param size
     * @param time
     * @return
     */
    public PageDto<StoreOpenDto> searchByStoreName(String storeName, int num, int size, Long time){
        Pageable pageable =  PageUtil.getPageable(num,size);
        Page<Store> page2 = storeRepository.findByStoreNameLikeAndCreateTimeLessThanEqual(storeName,time,pageable);
        List<Store> storeList = page2.getContent();
        List<StoreOpenDto> list = new ArrayList<>();
        for (Store store : storeList) {
            StoreOpenDto storeOpenDto = new StoreOpenDto(store.getId(),store.getUid(),store.getStoreName(),store.getStoreIcon(),store.getServicePhone(),store.getStoreIntro(),store.getBusinessLicense());
            list.add(storeOpenDto);
        }
        PageDto<StoreOpenDto> page3 = new PageDto<>(page2.getTotalElements(),list);
        return page3;
    }

    /**
     * 店铺id查询店铺
     * @return
     */
    public StoreOpenDto findBySid(String sid){
        Optional<Store> optional = storeRepository.findById(sid);
        if(!optional.isPresent()){
            return null;
        }
        Store store = optional.get();
        StoreOpenDto storeOpenDto = new StoreOpenDto(store.getId(),store.getUid(),store.getStoreName(),store.getStoreIcon(),store.getServicePhone(),store.getStoreIntro(),store.getBusinessLicense());
        return storeOpenDto;
    }

    /**
     * 手机号是否存在
     * @param phone
     * @return
     */
    public boolean validPhone(String phone){
        User user = userRepository.findByPhone(phone);
        if(user != null){
            return true;
        }
        return false;
    }

    /**
     * 昵称是否存在
     * @param nickName
     * @return
     */
    public boolean validNickName(String nickName){
        User user = userRepository.findByNickName(nickName);
        UserInfo userInfo = userInfoRepository.findByNickName(nickName);
        if(user != null || userInfo != null){
            return true;
        }
        return false;
    }

    /**
     * 注册:mismatch--验证码错误,registered--已注册,nickNameExist--昵称已存在
     * success--accessToken
     * @param phone
     * @param pwd
     * @param nickName
     * @param msCode
     * @return
     */
    public StateModel register(String phone,String pwd,String nickName,int msCode){
        if(!validSms(phone,msCode)){
            return new StateModel("mismatch");
        }
        if(validPhone(phone)){
            return new StateModel("registered");
        }
        if(validNickName(nickName)){
            return new StateModel("nickNameExist");
        }
        String salt = SHA256.generateSalt();
        User user = new User(null,phone, SHA256.encryptAddSalt(pwd,salt),salt,0,false,new BigDecimal("0"),new BigDecimal("0"),nickName,false,0L);
        userRepository.save(user);
        String accessToken = TokenUtil.generateToken(user.getId());
        Long mill = System.currentTimeMillis();
        UserToken userToken = new UserToken(null,user.getId(),RoleEnum.user, BackRoleEnum.user,false,0L,accessToken, UserStateEnum.normal,null,mill,mill);
        userTokenRepository.insert(userToken);
        UserInfo userInfo = new UserInfo(null,user.getId(),mill,nickName,null,SexEnum.unknown,null,null,null,null,null,null);
        userInfoRepository.insert(userInfo);
        return new StateModel("success",accessToken);
    }

    /**
     * 登录:mismatch--用户名密码不匹配,unsafety--不安全,ban--封禁,success-accessToken
     * @param phone
     * @param pwd
     * @return
     */
    public StateModel login(String phone,String pwd){
        User user = userRepository.findByPhone(phone);
        if(user == null){
            return new StateModel("mismatch",null);
        }
        if(user.getPwdFailCount() >= 10){
            //需要修改密码
            return new StateModel("unsafety",null);
        }
        if(!StringUtil.isEq(user.getPwd(),SHA256.encryptAddSalt(pwd,user.getPwdSalt()))){
            user = userRepository.findByIdForUpdate(user.getId());
            user.setPwdFailCount(user.getPwdFailCount() + 1);
            userRepository.save(user);
            return new StateModel("mismatch",null);
        }
        UserToken userToken = userTokenRepository.findByUid(user.getId());
        if(TokenUtil.isBan(userToken)){
            //用户被封禁
            return new StateModel("ban",userToken.getUserStateEndTime());
        }
        UserToken userTokenUp = new UserToken();
        userTokenUp.setAccessToken(TokenUtil.generateToken(userToken.getUid()));
        userTokenUp.setUid(userToken.getUid());
        userTokenUp.setLastLoginTime(System.currentTimeMillis());
        MongoUtil.updateNotNon("uid",userTokenUp,UserToken.class);
        return new StateModel("success",userTokenUp.getAccessToken());
    }

    /**
     * 修改密码:0--未注册,2--验证码错误,3--与原密码一致,1--修改成功
     * @param phone
     * @param newPwd
     * @param msCode
     * @return
     */
    public int alterPwd(String phone, String newPwd, int msCode){
        if(!validPhone(phone)){
            return 0;
        }
        if(!validSms(phone,msCode)){
            return 2;
        }
        User user = userRepository.findByPhone(phone);
        if(StringUtil.isEq(SHA256.encryptAddSalt(newPwd,user.getPwdSalt()),user.getPwd())){
            return 3;
        }
        user = userRepository.findByIdForUpdate(user.getId());
        String salt = SHA256.generateSalt();
        String pwd = SHA256.encryptAddSalt(newPwd,salt);
        user.setPwdFailCount(0);
        user.setPwd(pwd);
        user.setPwdSalt(salt);
        userRepository.save(user);
        return 1;
    }

    /**
     * 修改用户资料
     * @param uid
     * @param sex
     * @param birthday
     * @param province
     * @param city
     * @param openWxId
     * @param openQQ
     */
    public void alterUserData(int uid, SexEnum sex, Long birthday, String province, String city
            , String openWxId, String openQQ){ ;
        Map<String,Object> map = new HashMap<>();
        map.put("sex",sex);
        map.put("birthday",birthday);
        map.put("province",province);
        map.put("city",city);
        map.put("openWxId",openWxId);
        map.put("openQQ",openQQ);
        MongoUtil.updateMapStrictType("uid",uid,map,UserInfo.class);
    }

    /**
     * 修改头像
     */
    public void alterPortrait(int uid, String portrait){
        UserInfo userInfo = userInfoRepository.findByUid(uid);
        userInfo.setPortrait(portrait);
        QiniuUtil.delFilesBatch(userInfo.getPortrait());
        userInfo.setPortrait(portrait);
        userInfoRepository.save(userInfo);
    }

    /**
     * 修改个性签名
     * @param uid
     * @param personalSign
     */
    public void alterPersonalSign(int uid,String personalSign){
        UserInfo userInfo = userInfoRepository.findByUid(uid);
        userInfo.setPersonalSign(personalSign);
        userInfoRepository.save(userInfo);
    }

    /**
     * 修改昵称
     */
    public boolean alterNickName(int uid, String newNickName){
        if(validNickName(newNickName)){
            return false;
        }
        User user = userRepository.findByIdForUpdate(uid);
        user.setNickName(newNickName);
        userRepository.save(user);
        UserInfo userInfo = new UserInfo();
        userInfo.setUid(uid);
        userInfo.setNickName(newNickName);
        MongoUtil.updateNotNon("uid",userInfo,UserInfo.class);
        return true;
    }
}
