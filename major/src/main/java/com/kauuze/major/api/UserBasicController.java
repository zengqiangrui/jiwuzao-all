package com.kauuze.major.api;

import com.jiwuzao.common.include.DateTimeUtil;
import com.jiwuzao.common.include.JsonResult;
import com.jiwuzao.common.include.SHA256;
import com.jiwuzao.common.include.StringUtil;
import com.jiwuzao.common.pojo.common.*;
import com.jiwuzao.common.pojo.userBasic.*;
import com.jiwuzao.common.vo.user.UserCommentVO;
import com.kauuze.major.config.permission.Authorization;
import com.kauuze.major.config.permission.GreenWay;
import com.kauuze.major.include.StateModel;
import com.kauuze.major.service.GoodsService;
import com.kauuze.major.service.UserBasicService;
import com.kauuze.major.service.dto.userBasic.UserPrivateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-07 21:00
 */
@RestController
@RequestMapping("/userBasic")
@CrossOrigin
public class UserBasicController {
    @Autowired
    private UserBasicService userBasicService;

    @Autowired
    private GoodsService goodsService;


    @RequestMapping("/sendSms")
    public JsonResult sendSms(@Valid @RequestBody SendSmsPojo sendSmsPojo){
        if(!StringUtil.isEq(SHA256.encryptAddSalt(sendSmsPojo.getPhone(),"sendSms-kboot"),sendSmsPojo.getSha256())){
            return JsonResult.failure("发送失败");
        }
        if(userBasicService.sendSms(sendSmsPojo.getPhone()) != null){
            return JsonResult.success();
        }else{
            return JsonResult.failure("发送失败");
        }
    }

    @RequestMapping("/register")
    public JsonResult register(@Valid @RequestBody RegisterPojo registerPojo){
        StateModel stateModel = userBasicService.register(registerPojo.getPhone(),registerPojo.getPwd()
                ,registerPojo.getNickName(),registerPojo.getMsCode());
        if(stateModel.eq("mismatch")){
            return JsonResult.failure("验证码错误");
        }
        if(stateModel.eq("registered")){
            return JsonResult.failure("已注册");
        }
        if(stateModel.eq("nickNameExist")){
            return JsonResult.failure("昵称已存在");
        }
        return JsonResult.success(stateModel.getData());
    }

    @RequestMapping("/login")
    public JsonResult login(@Valid @RequestBody LoginPojo loginPojo){
        if(!StringUtil.isEq(SHA256.encryptAddSalt(loginPojo.getPhone(),"login-kboot"),loginPojo.getSha256())){
            return JsonResult.failure("登录失败");
        }
        StateModel stateModel = userBasicService.login(loginPojo.getPhone(),loginPojo.getPwd());
        if(stateModel.eq("mismatch")){
            return JsonResult.failure("用户名或密码不匹配");
        }
        if(stateModel.eq("unsafety")){
            return JsonResult.failure("需修改密码");
        }
        if(stateModel.eq("ban")){
            //返回封禁结束时间
            return JsonResult.failure("封禁直到:" + DateTimeUtil.covertDateView(Long.valueOf(String.valueOf(stateModel.getData()))));
        }
        return JsonResult.success(stateModel.getData());
    }

    @RequestMapping("/alterPwd")
    public JsonResult alterPwd(@Valid @RequestBody AlterPwdPojo alterPwdPojo){
        int result = userBasicService.alterPwd(alterPwdPojo.getPhone(), alterPwdPojo.getNewPwd(), alterPwdPojo.getMsCode());
        if(result == 0){
            return JsonResult.failure("未注册");
        }
        if(result == 2){
            return JsonResult.failure("验证码错误");
        }
        if(result == 3){
            return JsonResult.failure("与原密码一致");
        }
        return JsonResult.success();
    }

    @RequestMapping("/getUserPrivateDto")
    @Authorization
    public JsonResult getUserPrivateDto(@RequestAttribute int uid){
        UserPrivateDto userPrivateDto = userBasicService.getUserPrivateDto(uid);
        return JsonResult.success(userPrivateDto);
    }

    @RequestMapping("/validPhone")
    public JsonResult validPhone(@Valid @RequestBody PhonePojo phonePojo){
        if(userBasicService.validPhone(phonePojo.getPhone())){
            return JsonResult.success(true);
        }else{
            return JsonResult.success(false);
        }
    }

    @RequestMapping("/validNickName")
    public JsonResult validNickName(@Valid @RequestBody NickNamePojo nickNamePojo){
        if(userBasicService.validNickName(nickNamePojo.getNickName())){
            return JsonResult.success(true);
        }else{
            return JsonResult.success(false);
        }
    }

    @RequestMapping("/alterUserData")
    @Authorization
    public JsonResult alterUserData(@RequestAttribute int uid,@Valid @RequestBody AlterUserDataPojo alterUserDataPojo){
        userBasicService.alterUserData(uid, alterUserDataPojo.getSex(), alterUserDataPojo.getBirthday(), alterUserDataPojo.getProvince()
                , alterUserDataPojo.getCity(), alterUserDataPojo.getOpenWxId(), alterUserDataPojo.getOpenQQ());
        return JsonResult.success();
    }

    @RequestMapping("/alterNickName")
    @Authorization
    public JsonResult alterNickName(@RequestAttribute int uid,@Valid @RequestBody NickNamePojo nickNamePojo){
        if(userBasicService.alterNickName(uid, nickNamePojo.getNickName())){
            return JsonResult.success();
        }else{
            return JsonResult.failure();
        }

    }

    @RequestMapping("/alterPortrait")
    @Authorization
    public JsonResult alterPortrait(@RequestAttribute int uid, @Valid @RequestBody AlterPortraitPojo alterPortraitPojo){
        userBasicService.alterPortrait(uid, alterPortraitPojo.getPortrait());
        return JsonResult.success();
    }

    @RequestMapping("/alterPersonalSign")
    @Authorization
    public JsonResult alterPersonalSign(@RequestAttribute int uid, @Valid @RequestBody AlterPersonalSignPojo alterPersonalSignPojo){
        userBasicService.alterPersonalSign(uid, alterPersonalSignPojo.getPersonalSign());
        return JsonResult.success();
    }

    @RequestMapping("/alterGender")
    @Authorization
    public JsonResult alterGender(@RequestAttribute int uid, @Valid @RequestBody AlterGenderPojo alterGenderPojo){
        userBasicService.alterGender(uid, alterGenderPojo.getGender());
        return JsonResult.success();
    }

    @RequestMapping("/alterBirthday")
    @Authorization
    public JsonResult alterBirthday(@RequestAttribute int uid, @Valid @RequestBody AlterBirthdayPojo alterBirthdayPojo){
        userBasicService.alterBirthday(uid, alterBirthdayPojo.getBirthday());
        return JsonResult.success();
    }

    @RequestMapping("/getUserSimpleOpenDto")
    public JsonResult getUserSimpleOpenDto(@Valid @RequestBody UidPojo uidPojo){
        return JsonResult.success(userBasicService.getUserSimpleOpenDto(uidPojo.getUid()));
    }

    @RequestMapping("/getUserOpenDto")
    public JsonResult getUserOpenDto(@Valid @RequestBody UidPojo uidPojo){
        return JsonResult.success(userBasicService.getUserOpenDto(uidPojo.getUid()));
    }

    @RequestMapping("/getUserSimpleOpenDtos")
    public JsonResult getUserSimpleOpenDtos(@Valid @RequestBody UidsPojo uidsPojo){
        return JsonResult.success(userBasicService.getUserSimpleOpenDtos(uidsPojo.convert()));
    }

    @RequestMapping("/searchByNickName")
    public JsonResult searchByNickName(@Valid @RequestBody SearchByNickNamePojo searchByNickNamePojo){
        return JsonResult.success(userBasicService.searchByNickName(searchByNickNamePojo.getNickName(), searchByNickNamePojo.getPage().getNum()
                , searchByNickNamePojo.getPage().getSize(), searchByNickNamePojo.getPage().getTime()));
    }

    @RequestMapping("/searchByStoreName")
    public JsonResult searchByStoreName(@Valid @RequestBody SearchByStoreNamePojo searchByStoreNamePojo){
        PagePojo pagePojo = searchByStoreNamePojo.getPage();
        return JsonResult.success(userBasicService.searchByStoreName(searchByStoreNamePojo.getStoreName(),pagePojo.getNum(),pagePojo.getSize(),pagePojo.getTime()));
    }

    @RequestMapping("/findBySid")
    public JsonResult findBySid(@Valid @RequestBody SidPojo sidPojo){
        return JsonResult.success(userBasicService.findBySid(sidPojo.getSid()));
    }

    /**
     * 通过uid获取商品评论
     */
    @RequestMapping("/getUserComment")
    public JsonResult getUserComment(@Valid @RequestBody UidPojo pojo) {
        List<UserCommentVO> list = goodsService.getUserComment(pojo.getUid());
        if (list == null) {
            return JsonResult.failure();
        } else {
            return JsonResult.success(list);
        }
    }

}
