package com.kauuze.major.api;

import com.jiwuzao.common.domain.mysql.entity.WithdrawOrder;
import com.jiwuzao.common.include.JsonResult;
import com.jiwuzao.common.pojo.store.StoreIdPojo;
import com.jiwuzao.common.pojo.store.WithDrawAddPojo;
import com.jiwuzao.common.pojo.systemOrder.WithdrawPojo;
import com.jiwuzao.common.vo.store.StoreWithdrawVO;
import com.kauuze.major.config.permission.Merchant;
import com.kauuze.major.service.WithdrawService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/withdraw")
public class WithdrawController {

    @Autowired
    private WithdrawService withdrawService;

    @RequestMapping("/getWithdrawAble")
    @Merchant
    public JsonResult getWithdrawAble(@Valid @RequestBody StoreIdPojo pojo){
        StoreWithdrawVO withDraw = withdrawService.getWithDraw(pojo.getStoreId());
        if(null!= withDraw){
            return JsonResult.success(withDraw);
        }else{
            return JsonResult.failure();
        }
    }

    @RequestMapping("/addWithDraw")
    @Merchant
    public JsonResult addWithDraw(@RequestAttribute int uid,@Valid @RequestBody WithDrawAddPojo pojo){
//        withdrawService.merchantCreateCommonWithDraw();
        return null;
    }
}
