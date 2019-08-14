package com.kauuze.major.api;

import com.jiwuzao.common.domain.enumType.WithdrawStatusEnum;
import com.jiwuzao.common.domain.mongo.entity.userBastic.Store;
import com.jiwuzao.common.domain.mysql.entity.WithdrawOrder;
import com.jiwuzao.common.include.JsonResult;
import com.jiwuzao.common.pojo.store.StoreIdPojo;
import com.jiwuzao.common.pojo.store.WithDrawAddPojo;
import com.jiwuzao.common.pojo.store.WithdrawHandlePojo;
import com.jiwuzao.common.pojo.store.WithdrawSearchPojo;
import com.jiwuzao.common.pojo.systemOrder.WithdrawPojo;
import com.jiwuzao.common.vo.store.AllEarningVO;
import com.jiwuzao.common.vo.store.ManageWithdrawVO;
import com.jiwuzao.common.vo.store.StoreWithdrawVO;
import com.jiwuzao.common.vo.store.WithdrawHistoryVO;
import com.kauuze.major.config.permission.Cms;
import com.kauuze.major.config.permission.Merchant;
import com.kauuze.major.service.MerchantService;
import com.kauuze.major.service.UserBasicService;
import com.kauuze.major.service.WithdrawService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/withdraw")
public class WithdrawController {

    @Autowired
    private WithdrawService withdrawService;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private UserBasicService userBasicService;

    @RequestMapping("/getWithdrawAble")
    @Merchant
    public JsonResult getWithdrawAble(@RequestAttribute int uid) {
        Store merchantStore = merchantService.getMerchantStore(uid);
        StoreWithdrawVO withDraw = withdrawService.getWithDraw(merchantStore.getId());
        if (null != withDraw) {
            return JsonResult.success(withDraw);
        } else {
            return JsonResult.failure();
        }
    }

    @RequestMapping("/apply")
    @Merchant
    public JsonResult addWithDraw(@RequestAttribute int uid, @Valid @RequestBody WithDrawAddPojo pojo) {
        if (!userBasicService.checkPwd(uid, pojo.getPwd())) {
            return JsonResult.failure("密码错误");
        }
        WithdrawOrder withdrawOrder = withdrawService.merchantCreateCommonWithDraw(uid, pojo.getStoreId(), pojo.getRemitMoney(), pojo.getRemark());
        if (null != withdrawOrder) {
            return JsonResult.success();
        } else {
            return JsonResult.failure("申请提现失败");
        }
    }

    /**
     * 商家获取提现历史记录
     * @param pojo
     * @return
     */
    @RequestMapping("/showHistory")
    @Merchant
    public JsonResult showHistory(@Valid @RequestBody WithdrawPojo pojo) {
        List<WithdrawHistoryVO> collect = withdrawService.merchantShowHistory(pojo.getStoreId()).stream().map(withdrawOrder -> {
            WithdrawHistoryVO withdrawHistoryVO = new WithdrawHistoryVO();
            BeanUtils.copyProperties(withdrawOrder, withdrawHistoryVO);
            return withdrawHistoryVO;
        }).collect(Collectors.toList());
        return JsonResult.success(collect);
    }

    @RequestMapping("/getAllEarning")
    @Merchant
    public JsonResult getAllEarning(@Valid @RequestBody WithdrawPojo pojo){
        AllEarningVO allEarningVO  = withdrawService.merchantGetAllEarning(pojo.getStoreId());
        return JsonResult.success(allEarningVO);
    }

    @RequestMapping("/withdrawList")
    @Cms
    public JsonResult getWithdrawList(@Valid @RequestBody WithdrawSearchPojo pojo) {
        List<ManageWithdrawVO> manageWithdrawVOS = withdrawService.managerShowVO(pojo.getStatus());
        return JsonResult.success(manageWithdrawVOS);
    }

    @RequestMapping("/confirmWithdraw")
    @Cms
    public JsonResult confirmWithdraw(@Valid @RequestBody WithdrawHandlePojo pojo) {
        WithdrawOrder withdrawOrder = withdrawService.confirmWithDraw(pojo.getStoreId(), pojo.getWithdrawOrderNo());
        if (null != withdrawOrder) {
            return JsonResult.success();
        } else {
            return JsonResult.failure();
        }
    }

    @RequestMapping("/handleWithDraw")
    @Cms
    public JsonResult handleWithDraw(@RequestAttribute int uid, @Valid @RequestBody WithdrawHandlePojo pojo) {
        WithdrawOrder withdrawOrder = withdrawService.handleWithDraw(pojo.getStoreId(), pojo.getWithdrawOrderNo(), pojo.getSuccess(), pojo.getReason());
        if (null != withdrawOrder) {
            return JsonResult.success();
        } else {
            return JsonResult.failure();
        }
    }
}
