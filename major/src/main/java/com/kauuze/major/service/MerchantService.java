package com.kauuze.major.service;

import com.jiwuzao.common.domain.common.OrderUtil;
import com.jiwuzao.common.domain.enumType.*;
import com.jiwuzao.common.domain.mongo.entity.Goods;
import com.jiwuzao.common.domain.mongo.entity.SystemGoods;
import com.jiwuzao.common.domain.mongo.entity.userBastic.Store;
import com.jiwuzao.common.domain.mongo.entity.userBastic.VerifyActor;
import com.jiwuzao.common.domain.mysql.entity.PayOrder;
import com.jiwuzao.common.domain.mysql.entity.User;
import com.jiwuzao.common.domain.mysql.entity.WithdrawOrder;
import com.jiwuzao.common.include.PageDto;
import com.jiwuzao.common.vo.store.StoreSimpleVO;
import com.jiwuzao.common.vo.store.StoreVO;
import com.kauuze.major.ConfigUtil;
import com.kauuze.major.domain.common.EsUtil;
import com.kauuze.major.domain.mongo.repository.*;
import com.kauuze.major.domain.mysql.repository.PayOrderRepository;
import com.kauuze.major.domain.mysql.repository.UserRepository;
import com.kauuze.major.domain.mysql.repository.WithdrawOrderRepository;
import com.kauuze.major.include.StringUtil;
import com.kauuze.major.include.yun.WxPayUtil;
import com.kauuze.major.service.dto.merchant.MerchantUdpDto;
import com.qiniu.util.Json;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-06-10 11:25
 */
@Service
@Transactional(rollbackOn = Exception.class)
@Slf4j
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
    private UserBasicService userBasicService;
    @Autowired
    private UserInfoRepository userInfoRepository;

    /**
     * 申请商家认证
     */
    public String verifyActor(int uid, String trueName, String idcard, String frontIdCardPhoto
            , String handIdCardPhoto, String backIdCardPhoto, Long publicBankNo, String publicBankTrueName, OpeningBankEnum openingBank, String companyName, String uscc, String businessLicense, String otherSupportPhotos) {
        VerifyActor verifyActor = verifyActorRepository.findByUid(uid);
        if (verifyActor != null) {
            if (verifyActor.getAuditType() == AuditTypeEnum.refuse) {
                verifyActorRepository.deleteById(verifyActor.getId());
            } else {
                return "您已申请匠人认证";
            }
        }
        if (verifyActorRepository.findByIdcardAndAuditType(idcard, AuditTypeEnum.agree) != null) {
            return "该身份证已注册过匠人";
        }
        if (verifyActorRepository.findByUsccAndAuditType(uscc, AuditTypeEnum.agree) != null) {
            return "改企业已被实名注册过";
        }
        verifyActor = new VerifyActor(null, uid, System.currentTimeMillis(), trueName, idcard, frontIdCardPhoto, backIdCardPhoto, handIdCardPhoto, publicBankNo, publicBankTrueName, openingBank, companyName, uscc, businessLicense, otherSupportPhotos, AuditTypeEnum.wait, null, null);
        verifyActorRepository.insert(verifyActor);
        return null;
    }

    /**
     * 获取实名验证
     *
     * @param uid
     * @return
     */
    public VerifyActor getVerifyActor(int uid) {
        return verifyActorRepository.findByUid(uid);
    }

    /**
     * 获取商家金钱信息
     *
     * @return
     */
    public MerchantUdpDto getMerchantUdpDto(int uid) {
        User user = userRepository.findById(uid);
        List<WithdrawOrder> withdrawOrders = withdrawOrderRepository.findByUidAndWithdrawStatusNot(uid, WithdrawStatusEnum.success);
        BigDecimal onWithdrawOrder = new BigDecimal("0");
        for (WithdrawOrder withdrawOrder : withdrawOrders) {
            onWithdrawOrder.add(withdrawOrder.getRemitMoney());
        }
        MerchantUdpDto merchantUdpDto = new MerchantUdpDto(user.getDeposit(), user.getWithdrawal(), onWithdrawOrder, user.getTodayWithdrawal());
        return merchantUdpDto;
    }

    /**
     * 修改或开店铺
     */
    public String modifyOrOpenStore(int uid, String storeName, String storeIcon, String storeBgImg, String servicePhone, Integer mscode, String storeIntro, StoreStyleEnum storeStyle) {
        if (!userBasicService.validSms(servicePhone, mscode)) {
            return "验证码错误";
        }
        VerifyActor verifyActor = verifyActorRepository.findByUid(uid);
        if (verifyActor == null || verifyActor.getAuditType() != AuditTypeEnum.agree) {
            return "您未通过实名认证";
        }
        Optional<Store> opt = storeRepository.findByUid(uid);
        Store myStore;
        if (!opt.isPresent()) {
            if (storeRepository.findByStoreName(storeName) != null) {
                return "该店铺名称已被使用";
            }
            myStore = new Store();
            myStore.setCreateTime(System.currentTimeMillis());
        } else {
            myStore = opt.get();
            if (!StringUtil.isEq(myStore.getStoreName(), storeName) && storeRepository.findByStoreName(storeName) != null) {
                return "该店铺名称已被使用";
            }
            myStore.setModifyTime(System.currentTimeMillis());
        }
        myStore.setUid(uid);
        myStore.setStoreName(storeName);
        myStore.setStoreIcon(storeIcon);
        myStore.setStoreBgImg(storeBgImg);
        myStore.setServicePhone(servicePhone);
        myStore.setStoreIntro(storeIntro);
        myStore.setStoreStyle(storeStyle);
        return Json.encode(storeRepository.save(myStore));
    }

    /**
     * 缴纳保证金二维码
     *
     * @param uid
     * @param userIp
     * @return
     */
    public String getDepositQrCode(int uid, String userIp) {
        SystemGoods systemGoods = systemGoodsRepository.findByName(SystemGoodsNameEnum.deposit);
        PayOrder payOrder = new PayOrder(null, uid, System.currentTimeMillis(), null, true, false, true, null, systemGoods.getId(), systemGoods.getPrice(), systemGoods.getName().name, null, null, false, null, null);
        payOrderRepository.save(payOrder);
        payOrderRepository.save(payOrderRepository.findByIdForUpdate(payOrder.getId()).setPayOrderNo(OrderUtil.getOrderNo(payOrder.getId(), "p")));
        return WxPayUtil.generateWxPayQrCode(systemGoods.getId(), systemGoods.getName().name, payOrder.getPayOrderNo(), payOrder.getFinalPay(), ConfigUtil.payCallBackDomain + PayCallBackUrl.systemGoodsWxNoticeWxQrCode, userIp);
    }

    /**
     * 取出保证金:下架所有商品，15-30天提现
     *
     * @return
     */
    public String takeOutDeposit(int uid) {
        Optional<Store> opt = storeRepository.findByUid(uid);
        if (opt.isPresent() && opt.get().getViolation()) {
            return "你因违规，保证金被冻结";
        }
        List<Goods> list = goodsRepository.findByUid(uid);
        for (Goods goods : list) {
            Map<String, String> map = new HashMap<>();
            map.put("gid", goods.getGid());
            map.put("putaway", "false");
            map.put("soldOutTime", String.valueOf(System.currentTimeMillis()));
            EsUtil.modify(map);
        }
        User user = userRepository.findByIdForUpdate(uid);
        VerifyActor verifyActor = verifyActorRepository.findByUid(uid);
        WithdrawOrder withdrawOrder = new WithdrawOrder(null, uid, System.currentTimeMillis(), true, WithdrawStatusEnum.wait, null, user.getDeposit(), verifyActor.getBankNo(), verifyActor.getBankTrueName(), verifyActor.getOpeningBank(), "取出保证金", null, null, null);
        withdrawOrderRepository.save(withdrawOrder);
        withdrawOrderRepository.save(withdrawOrderRepository.findByIdForUpdate(withdrawOrder.getId()).setWithdrawOrderNo(OrderUtil.getOrderNo(withdrawOrder.getId(), "w")));
        return null;
    }

    /**
     * 商家提现
     *
     * @param uid
     * @param money
     * @return
     */
    public String withdraw(int uid, BigDecimal money) {
        User user = userRepository.findByIdForUpdate(uid);
        if (user.getTodayWithdrawal()) {
            return "今日已提现";
        }
        if (user.getWithdrawal().compareTo(money) < 0) {
            return "余额不足";
        }
        VerifyActor verifyActor = verifyActorRepository.findByUid(uid);
        if (verifyActor == null || verifyActor.getAuditType() != AuditTypeEnum.agree) {
            return "操作失败";
        }
        user.setWithdrawal(user.getWithdrawal().subtract(money));
        user.setTodayWithdrawal(true);
        userRepository.save(user);
        WithdrawOrder withdrawOrder = new WithdrawOrder(null, uid, System.currentTimeMillis(), false, WithdrawStatusEnum.wait, null, money, verifyActor.getBankNo(), verifyActor.getBankTrueName(), verifyActor.getOpeningBank(), null, null, null, null);
        withdrawOrderRepository.save(withdrawOrder);
        return null;
    }

    /**
     * 商户获取店铺信息
     *
     * @param uid
     * @return
     */
    public Store getMerchantStore(int uid) {
        return storeRepository.findByUid(uid).orElse(null);
    }

    /**
     * 根据店铺风格获取店铺分页信息
     * @param style
     * @param pageable
     * @return
     */
    public PageDto<StoreSimpleVO> getStoreByStyle(StoreStyleEnum style, Pageable pageable) {
        Page<Store> storesPage = storeRepository.findAllByStoreStyle(style, pageable);
        PageDto<StoreSimpleVO> pageDto = new PageDto<>();
        pageDto.setTotal(storesPage.getTotalElements());
        List<StoreSimpleVO> list = new ArrayList<>();
        for (Store store : storesPage.getContent()) {
            StoreSimpleVO storeSimpleVO = new StoreSimpleVO()
                    .setStoreIcon(store.getStoreIcon()).setStoreId(store.getId())
                    .setStoreName(store.getStoreName()).setStyle(store.getStoreStyle())
                    .setDescription(userBasicService.getUserOpenDto(store.getUid()).getPersonalSign());
                    list.add(storeSimpleVO);
        }
        pageDto.setContent(list);
        return pageDto;
    }

    public StoreVO getStoreVO(String storeId){
        StoreVO storeVO = new StoreVO();
        Optional<Store> byId = storeRepository.findById(storeId);
        if(byId.isPresent()){
            Store store = byId.get();
            storeVO.setPersonSign(userInfoRepository.findByUid(store.getUid()).getPersonalSign())
                    .setStoreBgImg(store.getStoreBgImg()).setStoreIntro(store.getStoreIntro()).setStoreName(store.getStoreName())
                    .setStoreId(storeId).setStoreIcon(store.getStoreIcon());
        }else {
            throw new RuntimeException("店铺没找到");
        }
        return storeVO;
    }
}
