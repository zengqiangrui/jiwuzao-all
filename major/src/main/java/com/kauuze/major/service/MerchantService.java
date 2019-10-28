package com.kauuze.major.service;

import com.jiwuzao.common.domain.enumType.*;
import com.jiwuzao.common.domain.mongo.entity.userBastic.Store;
import com.jiwuzao.common.domain.mongo.entity.userBastic.UserInfo;
import com.jiwuzao.common.domain.mongo.entity.userBastic.VerifyActor;
import com.jiwuzao.common.domain.mysql.entity.GoodsOrder;
import com.jiwuzao.common.domain.mysql.entity.PayOrder;
import com.jiwuzao.common.domain.mysql.entity.User;
import com.jiwuzao.common.domain.mysql.entity.WithdrawOrder;
import com.jiwuzao.common.exception.StoreException;
import com.jiwuzao.common.exception.excEnum.StoreExceptionEnum;
import com.jiwuzao.common.include.PageDto;
import com.jiwuzao.common.vo.store.AllEarningVO;
import com.jiwuzao.common.vo.store.StoreSimpleVO;
import com.jiwuzao.common.vo.store.StoreVO;
import com.kauuze.major.domain.mongo.repository.*;
import com.kauuze.major.domain.mysql.repository.GoodsOrderRepository;
import com.kauuze.major.domain.mysql.repository.PayOrderRepository;
import com.kauuze.major.domain.mysql.repository.UserRepository;
import com.kauuze.major.domain.mysql.repository.WithdrawOrderRepository;
import com.kauuze.major.include.StringUtil;
import com.kauuze.major.include.yun.TencentUtil;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private TencentUtil tencentUtil;
    @Autowired
    private UserBasicService userBasicService;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private GoodsOrderRepository goodsOrderRepository;
    @Autowired
    private PayOrderRepository payOrderRepository;

    /**
     * 申请商家认证
     */
    public String verifyActor(int uid, String trueName, String idcard, String frontIdCardPhoto
            , String handIdCardPhoto, String backIdCardPhoto, String publicBankNo, String publicBankTrueName, String openingBank, String companyName, String uscc, String businessLicense, String accountOpenLicence, String otherSupportPhotos) {
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
        verifyActor = new VerifyActor(null, uid, System.currentTimeMillis(), trueName, idcard, frontIdCardPhoto, backIdCardPhoto, handIdCardPhoto, publicBankNo, publicBankTrueName, openingBank, accountOpenLicence, companyName, uscc, businessLicense, otherSupportPhotos, AuditTypeEnum.wait, null, null);
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
        return new MerchantUdpDto(user.getDeposit(), user.getWithdrawal(), onWithdrawOrder, user.getTodayWithdrawal());
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

//    /**
//     * 缴纳保证金二维码
//     *
//     * @param uid
//     * @param userIp
//     * @return
//     */
//    public String getDepositQrCode(int uid, String userIp) {
//        SystemGoods systemGoods = systemGoodsRepository.findByName(SystemGoodsNameEnum.deposit);
//        String sid = storeRepository.findByUid(uid).get().getId();
//        PayOrder payOrder = new PayOrder(null, uid, sid, System.currentTimeMillis(), null, true, false, true, null, systemGoods.getId(), systemGoods.getPrice(), systemGoods.getPrice(), systemGoods.getName().name, null, null, false, null, null);
//        payOrderRepository.save(payOrder);
//        payOrderRepository.save(payOrderRepository.findByIdForUpdate(payOrder.getId()).setPayOrderNo(OrderUtil.getOrderNo(payOrder.getId(), "p")));
//        return WxPayUtil.generateWxPayQrCode(systemGoods.getId(), systemGoods.getName().name, payOrder.getPayOrderNo(), payOrder.getFinalPay(), ConfigUtil.payCallBackDomain + PayCallBackUrl.systemGoodsWxNoticeWxQrCode, userIp);
//    }

    /**
     * 取出保证金:下架所有商品，15-30天提现
     *
     * @return
     */
//    public String takeOutDeposit(int uid) {
//        Optional<Store> opt = storeRepository.findByUid(uid);
//        if (opt.isPresent() && opt.get().getViolation()) {
//            return "你因违规，保证金被冻结";
//        }
//        List<Goods> list = goodsRepository.findByUid(uid);
//        for (Goods goods : list) {
//            Map<String, String> map = new HashMap<>();
//            map.put("gid", goods.getGid());
//            map.put("putaway", "false");
//            map.put("soldOutTime", String.valueOf(System.currentTimeMillis()));
//            EsUtil.modify(map);
//        }
//        User user = userRepository.findByIdForUpdate(uid);
//        VerifyActor verifyActor = verifyActorRepository.findByUid(uid);
//        WithdrawOrder withdrawOrder = new WithdrawOrder(null, uid, opt.get().getId(),System.currentTimeMillis(), true, WithdrawStatusEnum.wait, null, user.getDeposit(), verifyActor.getBankNo(), verifyActor.getBankTrueName(), verifyActor.getOpeningBank(), "取出保证金", null, null, null);
//        withdrawOrderRepository.save(withdrawOrder);
//        withdrawOrderRepository.save(withdrawOrderRepository.findByIdForUpdate(withdrawOrder.getId()).setWithdrawOrderNo(OrderUtil.getOrderNo(withdrawOrder.getId(), "w")));
//        return null;
//    }


    /**
     * 商户获取店铺信息
     *
     * @param uid
     * @return
     */
    public Store getMerchantStore(int uid) {
        Optional<Store> byUid = storeRepository.findByUid(uid);
        if (byUid.isPresent()) {
            return byUid.get();
        } else {
            throw new StoreException(StoreExceptionEnum.STORE_NOT_FOUND);
        }
    }

    /**
     * 根据订单发送一条短信给商家
     *
     * @param goodsOrder
     * @return
     */
    public boolean sendDeliverMessage(GoodsOrder goodsOrder) {
        String[] param = new String[2];
        param[0] = goodsOrder.getGoodsTitle();
        param[1] = goodsOrder.getSpecClass();
        log.info("发送短信：{}", goodsOrder);
        return tencentUtil.sendDeliverNotice(getMerchantStoreById(goodsOrder.getSid()).getServicePhone(), param);
    }

    private Store getMerchantStoreById(String sid) {
        Optional<Store> optional = storeRepository.findById(sid);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new StoreException(StoreExceptionEnum.STORE_NOT_FOUND);
        }
    }


    /**
     * 根据店铺风格获取店铺分页信息
     *
     * @param style
     * @param pageable
     * @return
     */
    public PageDto<StoreSimpleVO> getStoreByStyle(StoreStyleEnum style, Pageable pageable) {
        Page<Store> storesPage = storeRepository.findAllByStoreStyle(style, pageable);
        PageDto<StoreSimpleVO> pageDto = new PageDto<>();
        pageDto.setTotal(storesPage.getTotalElements());
        List<StoreSimpleVO> list = storesPage.getContent().stream()
                .filter(store -> !store.getViolation())
                .map(store -> new StoreSimpleVO()
                        .setStoreIcon(store.getStoreIcon()).setStoreId(store.getId()).setArtisanName(getVerifyActor(store.getUid()).getTrueName())
                        .setStoreName(store.getStoreName()).setStyle(store.getStoreStyle())
                        .setDescription(userBasicService.getUserOpenDto(store.getUid()).getPersonalSign()))
                .collect(Collectors.toList());
        pageDto.setContent(list);
        return pageDto;
    }

    public StoreVO getStoreVO(String storeId) {
        StoreVO storeVO = new StoreVO();
        Optional<Store> byId = storeRepository.findById(storeId);
        if (byId.isPresent()) {
            Store store = byId.get();
            UserInfo info = userInfoRepository.findByUid(store.getUid());
            storeVO.setPersonSign(info.getPersonalSign()).setUid(store.getUid())
                    .setStoreBgImg(store.getStoreBgImg()).setStoreIntro(store.getStoreIntro()).setStoreName(store.getStoreName())
                    .setStoreId(storeId).setStoreIcon(store.getStoreIcon());
        } else {
            throw new RuntimeException("店铺没找到");
        }
        return storeVO;
    }

    public AllEarningVO getStoreTurnover(String storeId) {
        List<GoodsOrder> list = goodsOrderRepository.findAllBySid(storeId);
        AllEarningVO vo = new AllEarningVO();
        vo.setStoreId(storeId);
        BigDecimal bigDecimal =
                list.stream().filter(goodsOrder -> goodsOrder.getOrderExStatus() != OrderExStatusEnum.exception)
                        .filter(goodsOrder -> goodsOrder.getOrderStatus() != OrderStatusEnum.refund)
                        .filter(goodsOrder -> {
                            Optional<PayOrder> optional = payOrderRepository.findById(goodsOrder.getPayid());
                            if (!optional.isPresent()) {
                                return false;
                            } else {
                                PayOrder payOrder = optional.get();
                                return payOrder.getPay();
                            }
                        }).map(GoodsOrder::getFinalPay).reduce(BigDecimal.ZERO, BigDecimal::add);
        log.info("总营业额{}", bigDecimal);
        vo.setTurnover(bigDecimal);
        return vo;
    }
}
