package com.kauuze.manager.service;

import com.kauuze.manager.domain.common.EsUtil;
import com.kauuze.manager.domain.common.MongoUtil;
import com.kauuze.manager.domain.enumType.AuditTypeEnum;
import com.kauuze.manager.domain.enumType.RoleEnum;
import com.kauuze.manager.domain.enumType.UserStateEnum;
import com.kauuze.manager.domain.es.entity.GoodsEs;
import com.kauuze.manager.domain.es.repository.GoodsRepositoryEs;
import com.kauuze.manager.domain.mongo.entity.userBastic.Store;
import com.kauuze.manager.domain.mongo.entity.userBastic.UserToken;
import com.kauuze.manager.domain.mongo.entity.userBastic.VerifyActor;
import com.kauuze.manager.domain.mongo.repository.StoreRepository;
import com.kauuze.manager.domain.mongo.repository.UserTokenRepository;
import com.kauuze.manager.domain.mongo.repository.VerifyActorRepository;
import com.kauuze.manager.include.DateTimeUtil;
import com.kauuze.manager.include.PageDto;
import com.kauuze.manager.include.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-06-10 13:51
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class MerchantService {
    @Autowired
    private VerifyActorRepository verifyActorRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private GoodsRepositoryEs goodsRepositoryEs;
    @Autowired
    private UserTokenRepository userTokenRepository;

    /**
     * 查询各实名认证审批状态：按新插入排序
     * @param auditType
     * @return
     */
    public PageDto<VerifyActor> findVerifyActorByAuditType(AuditTypeEnum auditType, int num, int size, long time){
        Pageable pageable = PageUtil.getNewsInsert(num,size);
        Page<VerifyActor> verifyActors;
        if(auditType != null){
            verifyActors = verifyActorRepository.findByAuditTypeAndCreateTimeLessThanEqual(auditType,time,pageable);
        }else{
            verifyActors = verifyActorRepository.findByCreateTimeLessThanEqual(time,pageable);
        }
        PageDto<VerifyActor> verifyActorPageDto = new PageDto<>();
        verifyActorPageDto.setTotal(verifyActors.getTotalElements());
        verifyActorPageDto.setContent(verifyActors.getContent());
        return verifyActorPageDto;
    }

    /**
     * 审批用户实名认证
     */
    public String auditVerifyActor(int uid, AuditTypeEnum auditType){
        VerifyActor verifyActor = verifyActorRepository.findByUid(uid);
        if(verifyActor == null || verifyActor.getAuditType() != AuditTypeEnum.wait){
            return "操作失败";
        }
        if(verifyActorRepository.findByIdcardAndAuditType(verifyActor.getIdcard(), AuditTypeEnum.agree) != null){
            return "该身份证已被实名注册过";
        }
        if(verifyActorRepository.findByUsccAndAuditType(verifyActor.getUscc(),AuditTypeEnum.agree) != null){
            return "改企业已被实名注册过";
        }
        VerifyActor verifyActor2 = new VerifyActor();
        verifyActor2.setId(verifyActor.getId());
        verifyActor2.setAuditTime(System.currentTimeMillis());
        verifyActor2.setAuditType(auditType);
        MongoUtil.updateNotNon("id",verifyActor2,VerifyActor.class);
        UserToken userToken = new UserToken();
        userToken.setUid(uid);
        userToken.setRole(RoleEnum.merchant);
        MongoUtil.updateNotNon("uid",userToken,UserToken.class);
        return null;
    }

    /**
     * 封禁商家:下架所有商品，不允许上架。
     */
    public void banMerchant(int uid,String violationCause){
        Store store = storeRepository.findByUid(uid);
        if(store != null){
            Store store2 = new Store();
            store2.setId(store.getId());
            store2.setViolation(true);
            store2.setViolationCause(violationCause);
            MongoUtil.updateNotNon("id",store2,Store.class);
        }
        List<GoodsEs> list = goodsRepositoryEs.findByUid(uid);
        for (GoodsEs goodsEs : list) {
            Map<String,String> map = new HashMap<>();
            map.put("gid", goodsEs.getGid());
            map.put("putaway","false");
            map.put("soldOutTime",String.valueOf(System.currentTimeMillis()));
            EsUtil.modify(map);
        }
        UserToken userToken = new UserToken();
        userToken.setUid(uid);
        userToken.setUserState(UserStateEnum.ban);
        userToken.setUserStateEndTime(System.currentTimeMillis() + DateTimeUtil.getOneYearMill()*70);
        MongoUtil.updateNotNon("uid",userToken,UserToken.class);
    }

    /**
     * 实名认证非法:修改认证资料为非法xx
     * @param uid
     * @return
     */
    public void illegalAuditVerify(int uid,String violationCause){
        banMerchant(uid,violationCause);
        VerifyActor verifyActor = new VerifyActor();
        verifyActor.setUid(uid);
        verifyActor.setUscc("违规" + uid);
        verifyActor.setIdcard("违规" + uid);
        MongoUtil.updateNotNon("uid",verifyActor,VerifyActor.class);
    }
}
