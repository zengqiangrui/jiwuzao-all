package com.kauuze.major.service;

import com.kauuze.major.ConfigUtil;
import com.kauuze.major.domain.common.EsUtil;
import com.kauuze.major.domain.common.OrderUtil;
import com.kauuze.major.domain.enumType.*;
import com.kauuze.major.domain.es.entity.Goods;
import com.kauuze.major.domain.es.repository.GoodsRepository;
import com.kauuze.major.domain.mongo.entity.SystemGoods;
import com.kauuze.major.domain.mongo.entity.userBastic.Store;
import com.kauuze.major.domain.mongo.entity.userBastic.StoreAudit;
import com.kauuze.major.domain.mongo.entity.userBastic.VerifyActor;
import com.kauuze.major.domain.mongo.repository.StoreAuditRepository;
import com.kauuze.major.domain.mongo.repository.StoreRepository;
import com.kauuze.major.domain.mongo.repository.SystemGoodsRepository;
import com.kauuze.major.domain.mongo.repository.VerifyActorRepository;
import com.kauuze.major.domain.mysql.entity.PayOrder;
import com.kauuze.major.domain.mysql.entity.User;
import com.kauuze.major.domain.mysql.entity.WithdrawOrder;
import com.kauuze.major.domain.mysql.repository.PayOrderRepository;
import com.kauuze.major.domain.mysql.repository.UserRepository;
import com.kauuze.major.domain.mysql.repository.WithdrawOrderRepository;
import com.kauuze.major.include.StringUtil;
import com.kauuze.major.include.yun.WxPayUtil;
import com.kauuze.major.service.dto.merchant.MerchantUdpDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-06-10 11:25
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class MerchantService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VerifyActorRepository verifyActorRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private WithdrawOrderRepository withdrawOrderRepository;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private SystemGoodsRepository systemGoodsRepository;
    @Autowired
    private PayOrderRepository payOrderRepository;
    @Autowired
    private StoreAuditRepository storeAuditRepository;
    @Autowired
    private UserBasicService userBasicService;

    /**
     * 申请商家认证
     */
    public String verifyActor(int uid, String trueName, String idcard, String frontIdCardPhoto
            , String handIdCardPhoto, String backIdCardPhoto, Long publicBankNo, String publicBankTrueName, OpeningBankEnum openingBank, String companyName, String uscc, String businessLicense, String otherSupportPhotos){
        VerifyActor verifyActor = verifyActorRepository.findByUid(uid);
        if(verifyActor != null){
            if(verifyActor.getAuditType() == AuditTypeEnum.refuse){
                verifyActorRepository.deleteById(verifyActor.getId());
            }else{
                return "您已申请匠人认证";
            }
        }
        if(verifyActorRepository.findByIdcardAndAuditType(idcard,AuditTypeEnum.agree) != null){
            return "该身份证已注册过匠人";
        }
        if(verifyActorRepository.findByUsccAndAuditType(uscc,AuditTypeEnum.agree) != null){
            return "改企业已被实名注册过";
        }
        verifyActor = new VerifyActor(null,uid,System.currentTimeMillis(),trueName,idcard,frontIdCardPhoto,backIdCardPhoto,handIdCardPhoto,publicBankNo,publicBankTrueName,openingBank,companyName,uscc,businessLicense,otherSupportPhotos,AuditTypeEnum.wait,null,null);
        verifyActorRepository.insert(verifyActor);
        return null;
    }

    /**
     * 获取实名验证
     * @param uid
     * @return
     */
    public VerifyActor getVerifyActor(int uid){
        return verifyActorRepository.findByUid(uid);
    }

    /**
     * 获取商家金钱信息
     * @return
     */
    public MerchantUdpDto getMerchantUdpDto(int uid){
        User user = userRepository.findById(uid);
        List<WithdrawOrder> withdrawOrders = withdrawOrderRepository.findByUidAndWithdrawStatusNot(uid, WithdrawStatusEnum.success);
        BigDecimal onWithdrawOrder = new BigDecimal("0");
        for (WithdrawOrder withdrawOrder : withdrawOrders) {
            onWithdrawOrder.add(withdrawOrder.getRemitMoney());
        }
        MerchantUdpDto merchantUdpDto = new MerchantUdpDto(user.getDeposit(),user.getWithdrawal(),onWithdrawOrder,user.getTodayWithdrawal());
        return merchantUdpDto;
    }

    /**
     * 修改或开店铺
     */
    public String modifyOrOpenStore(int uid, String storeName, String storeIcon, String servicePhone,Integer mscode,String storeIntro, String businessLicense){
        if(!userBasicService.validSms(servicePhone,mscode)){
            return "验证码错误";
        }
        VerifyActor verifyActor = verifyActorRepository.findByUid(uid);
        if(verifyActor == null || verifyActor.getAuditType() != AuditTypeEnum.agree){
            return "您未通过实名认证";
        }
        Store store = storeRepository.findByUid(uid);
        if(store == null){
            if(storeRepository.findByStoreName(storeName) != null){
                return "该店铺名称已被使用";
            }
        }else{
            if(!StringUtil.isEq(store.getStoreName(),storeName) && storeRepository.findByStoreName(storeName) != null){
                return "该店铺名称已被使用";
            }
        }
        StoreAudit storeAudit = storeAuditRepository.findByUid(uid);
        if(storeAudit == null){
            storeAudit = new StoreAudit();
            storeAudit.setCreateTime(System.currentTimeMillis());
        }else{
            if(storeAudit.getAuditType() == AuditTypeEnum.wait){
                return "已有申请未审批";
            }
        }
        storeAudit.setStoreName(storeName);
        storeAudit.setStoreIcon(storeIcon);
        storeAudit.setServicePhone(servicePhone);
        storeAudit.setStoreIntro(storeIntro);
        storeAudit.setBusinessLicense(businessLicense);
        storeAudit.setAuditType(AuditTypeEnum.wait);
        storeAuditRepository.save(storeAudit);
        return null;
    }

    /**
     * 缴纳保证金二维码
     * @param uid
     * @param userIp
     * @return
     */
    public String getDepositQrCode(int uid,String userIp){
        SystemGoods systemGoods = systemGoodsRepository.findByName(SystemGoodsNameEnum.deposit);
        PayOrder payOrder = new PayOrder(null,uid,System.currentTimeMillis(),null,true,false,true,null,systemGoods.getId(),systemGoods.getPrice(),systemGoods.getName().name,null,null,false,null,null);
        payOrderRepository.save(payOrder);
        payOrderRepository.save(payOrderRepository.findByIdForUpdate(payOrder.getId()).setPayOrderNo(OrderUtil.getOrderNo(payOrder.getId(),"p")));
        return WxPayUtil.generateWxPayQrCode(systemGoods.getId(),systemGoods.getName().name,payOrder.getPayOrderNo(),payOrder.getFinalPay(), ConfigUtil.payCallBackDomain + PayCallBackUrl.systemGoodsWxNoticeWxQrCode,userIp);
    }

    /**
     * 取出保证金:下架所有商品，15-30天提现
     * @return
     */
    public String takeOutDeposit(int uid){
        Store store = storeRepository.findByUid(uid);
        if(store != null && store.getViolation()){
            return "你因违规，保证金被冻结";
        }
        List<Goods> list = goodsRepository.findByUid(uid);
        for (Goods goods : list) {
            Map<String,String> map = new HashMap<>();
            map.put("gid", goods.getGid());
            map.put("putaway","false");
            map.put("soldOutTime",String.valueOf(System.currentTimeMillis()));
            EsUtil.modify(map);
        }
        User user = userRepository.findByIdForUpdate(uid);
        VerifyActor verifyActor = verifyActorRepository.findByUid(uid);
        WithdrawOrder withdrawOrder = new WithdrawOrder(null,uid,System.currentTimeMillis(),true, WithdrawStatusEnum.wait,null,user.getDeposit(),verifyActor.getBankNo(),verifyActor.getBankTrueName(),verifyActor.getOpeningBank(),"取出保证金",null,null,null);
        withdrawOrderRepository.save(withdrawOrder);
        withdrawOrderRepository.save(withdrawOrderRepository.findByIdForUpdate(withdrawOrder.getId()).setWithdrawOrderNo(OrderUtil.getOrderNo(withdrawOrder.getId(),"w")));
        return null;
    }

    /**
     * 商家提现
     * @param uid
     * @param money
     * @return
     */
    public String withdraw(int uid,BigDecimal money){
        User user = userRepository.findByIdForUpdate(uid);
        if(user.getTodayWithdrawal()){
            return "今日已提现";
        }
        if(user.getWithdrawal().compareTo(money) < 0){
            return "余额不足";
        }
        VerifyActor verifyActor = verifyActorRepository.findByUid(uid);
        if(verifyActor == null || verifyActor.getAuditType() != AuditTypeEnum.agree){
            return "操作失败";
        }
        user.setWithdrawal(user.getWithdrawal().subtract(money));
        user.setTodayWithdrawal(true);
        userRepository.save(user);
        WithdrawOrder withdrawOrder = new WithdrawOrder(null,uid,System.currentTimeMillis(),false,WithdrawStatusEnum.wait,null,money,verifyActor.getBankNo(),verifyActor.getBankTrueName(),verifyActor.getOpeningBank(),null,null,null,null);
        withdrawOrderRepository.save(withdrawOrder);
        return null;
    }
}
