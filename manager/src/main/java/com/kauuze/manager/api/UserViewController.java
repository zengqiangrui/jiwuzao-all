package com.kauuze.manager.api;

import com.jiwuzao.common.pojo.common.PhonePojo;
import com.jiwuzao.common.pojo.common.UidPojo;
import com.kauuze.manager.api.pojo.userView.FindByNickNamePojo;
import com.kauuze.manager.config.permission.Cms;
import com.kauuze.manager.config.permission.Root;
import com.kauuze.manager.include.JsonResult;
import com.kauuze.manager.service.MerchantService;
import com.kauuze.manager.service.UserViewService;
import com.qiniu.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-30 14:38
 */
@RestController
@RequestMapping("/userView")
public class UserViewController {
    @Autowired
    private UserViewService userViewService;
    @Autowired
    private MerchantService merchantService;

    @RequestMapping("/findByUid")
    @Cms
    public JsonResult findByUid(@Valid @RequestBody UidPojo uidPojo){
        return JsonResult.success(userViewService.findByUid(uidPojo.getUid()));
    }

    @RequestMapping("/findByNickName")
    @Cms
    public JsonResult findByNickName(@Valid @RequestBody FindByNickNamePojo findByNickNamePojo){
        return JsonResult.success(userViewService.findByNickName(findByNickNamePojo.getNickName(),
                findByNickNamePojo.getNum(), findByNickNamePojo.getSize()));
    }

    @RequestMapping("/findByPhone")
    @Cms
    public JsonResult findByPhone(@Valid @RequestBody PhonePojo phonePojo){
        return JsonResult.success(userViewService.findByPhone(phonePojo.getPhone()));
    }

    @RequestMapping("/findAllCms")
    @Root
    public JsonResult findAllCms(@RequestParam int page){
        return JsonResult.success(userViewService.findAllCms(page, 20));
    }

    @RequestMapping("/illegalNickname")
    @Cms
    public JsonResult illegalNickname(@Valid @RequestBody UidPojo uidPojo){
        if(userViewService.illegalNickname(uidPojo.getUid())){
            return JsonResult.success();
        }else{
            return JsonResult.failure();
        }
    }

    @RequestMapping("/illegalPortrait")
    @Cms
    public JsonResult illegalPortrait(@Valid @RequestBody UidPojo uidPojo){
        if(userViewService.illegalPortrait(uidPojo.getUid())){
            return JsonResult.success();
        }else{
            return JsonResult.failure();
        }
    }

    @RequestMapping("/illegalPersonalSign")
    @Cms
    public JsonResult illegalPersonalSign(@Valid @RequestBody UidPojo uidPojo){
        if(userViewService.illegalPersonalSign(uidPojo.getUid())){
            return JsonResult.success();
        }else {
            return JsonResult.failure();
        }
    }

    @RequestMapping("/illegalOpenWxIdAndQQ")
    @Cms
    public JsonResult illegalOpenWxIdAndQQ(@Valid @RequestBody UidPojo uidPojo){
        if(userViewService.illegalOpenWxIdAndQQ(uidPojo.getUid())){
            return JsonResult.success();
        }else{
            return JsonResult.failure();
        }
    }

    @RequestMapping("/getVerifyActor")
    @Cms
    public JsonResult getVerifyActor(@RequestBody @Valid UidPojo pojo){
        return JsonResult.success(merchantService.findVerifyActorByUid(pojo.getUid()));
    }

}
