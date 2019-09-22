package com.kauuze.major.api;

import com.jiwuzao.common.domain.mongo.entity.userBastic.Store;
import com.jiwuzao.common.include.PageDto;
import com.jiwuzao.common.pojo.common.IpAddressPojo;
import com.jiwuzao.common.pojo.common.PagePojo;
import com.jiwuzao.common.pojo.merchant.OpenStorePojo;
import com.jiwuzao.common.pojo.merchant.VerifyActorPojo;
import com.jiwuzao.common.pojo.store.StoreIdPojo;
import com.jiwuzao.common.pojo.store.StorePagePojo;
import com.jiwuzao.common.pojo.systemOrder.WithdrawPojo;
import com.jiwuzao.common.vo.store.AllEarningVO;
import com.jiwuzao.common.vo.store.StoreSimpleVO;
import com.jiwuzao.common.vo.store.StoreVO;
import com.kauuze.major.config.permission.Authorization;
import com.kauuze.major.config.permission.Merchant;
import com.kauuze.major.include.JsonResult;
import com.kauuze.major.include.StringUtil;
import com.kauuze.major.service.MerchantService;
import com.qiniu.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-06-10 11:25
 */
@RestController
@RequestMapping("/merchant")
public class MerchantController {
    @Autowired
    private MerchantService merchantService;

    /**
     * 认证匠人
     *
     * @param uid
     * @param verifyActorPojo
     * @return
     */
    @RequestMapping("/verifyActor")
    @Authorization
    public JsonResult verifyActor(@RequestAttribute int uid, @Valid @RequestBody VerifyActorPojo verifyActorPojo) {
        System.out.println("申请匠人id" + uid);
        String result = merchantService.verifyActor(uid, verifyActorPojo.getBankTrueName(), verifyActorPojo.getIdcard(), verifyActorPojo.getFrontIdCardPhoto(), verifyActorPojo.getHandIdCardPhoto(), verifyActorPojo.getBackIdCardPhoto(), verifyActorPojo.getBankNo(), verifyActorPojo.getBankTrueName(), verifyActorPojo.getOpeningBank(), verifyActorPojo.getCompanyName(), verifyActorPojo.getUscc(), verifyActorPojo.getBusinessLicense(), verifyActorPojo.getAccountOpenLicence(),verifyActorPojo.getOtherSupportPhotos());
        if (StringUtil.isBlank(result)) {
            return JsonResult.success();
        } else {
            return JsonResult.failure(result);
        }
    }

    @RequestMapping("/getVerifyActor")
    @Authorization
    public JsonResult getVerifyActor(@RequestAttribute int uid) {
        return JsonResult.success(merchantService.getVerifyActor(uid));
    }

    @RequestMapping("/getMerchantUdpDto")
    @Merchant
    public JsonResult getMerchantUdpDto(@RequestAttribute int uid) {
        return JsonResult.success(merchantService.getMerchantUdpDto(uid));
    }

    @RequestMapping("/modifyOrOpenStore")
    @Merchant
    public JsonResult modifyOrOpenStore(@RequestAttribute int uid, @Valid @RequestBody OpenStorePojo openStorePojo) {
        return JsonResult.success(merchantService.modifyOrOpenStore(uid, openStorePojo.getStoreName(), openStorePojo.getStoreIcon(), openStorePojo.getStoreBgImg(), openStorePojo.getServicePhone(), openStorePojo.getMsCode(), openStorePojo.getStoreIntro(), openStorePojo.getStoreStyle()));
    }

    /**
     * 商户获取自己店铺信息
     *
     * @param uid
     * @return
     */
    @RequestMapping("/getMerchantStore")
    @Merchant
    public JsonResult getMerchantStore(@RequestAttribute int uid) {
        Store store = merchantService.getMerchantStore(uid);
        if (null != store) {
            return JsonResult.success(store);
        } else {
            return JsonResult.failure("未找到店铺,请开通");
        }
    }
//
//    @RequestMapping("/getDepositQrCode")
//    @Merchant
//    public JsonResult getDepositQrCode(@RequestAttribute int uid, @Valid @RequestBody IpAddressPojo ipAddressPojo) {
//        String qrCodeUrl = merchantService.getDepositQrCode(uid, ipAddressPojo.getIpAddress());
//        if (qrCodeUrl != null) {
//            return JsonResult.success(qrCodeUrl);
//        } else {
//            return JsonResult.failure();
//        }
//    }

//    @RequestMapping("/takeOutDeposit")
//    @Merchant
//    public JsonResult takeOutDeposit(@RequestAttribute int uid) {
//        String result = merchantService.takeOutDeposit(uid);
//        if (result != null) {
//            return JsonResult.success();
//        } else {
//            return JsonResult.failure(result);
//        }
//    }


    @RequestMapping("/getStorePage")
    public JsonResult getStorePage(@Valid@RequestBody StorePagePojo pagePojo) {
        PageDto<StoreSimpleVO> pageDto = merchantService.getStoreByStyle(pagePojo.getStoreStyle(), PageRequest.of(pagePojo.getPageNum(), pagePojo.getPageSize(), Sort.by(pagePojo.getOrderBy())));
        return JsonResult.success(pageDto);
    }

    @RequestMapping("/getStoreById")
    public JsonResult getStoreById(@Valid @RequestBody StoreIdPojo pojo){
        StoreVO storeVO = merchantService.getStoreVO(pojo.getStoreId());
        return JsonResult.success(storeVO);
    }

    @RequestMapping("/getStoreTurnover")
    @Merchant
    public JsonResult getStoreTurnover(@Valid @RequestBody StoreIdPojo pojo){
        AllEarningVO vo = merchantService.getStoreTurnover(pojo.getStoreId());
        return JsonResult.success(vo);
    }
}

