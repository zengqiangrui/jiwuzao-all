package com.kauuze.manager.api;

import com.jiwuzao.common.pojo.common.UidPojo;
import com.kauuze.manager.api.pojo.userPermission.ForbidPojo;
import com.kauuze.manager.config.permission.Cms;
import com.kauuze.manager.config.permission.Root;
import com.kauuze.manager.include.JsonResult;
import com.kauuze.manager.service.UserPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-07 21:00
 */
@RestController
@RequestMapping("/userPermission")
public class UserPermissionController {
    @Autowired
    private UserPermissionService userPermissionService;

    @RequestMapping("/forbid")
    @Cms
    public JsonResult forbid(@Valid @RequestBody ForbidPojo forbidPojo){
        String result = userPermissionService.forbid(forbidPojo.getUid(),forbidPojo.getUserState(),forbidPojo.getUserStateEndTime());
        if(result == null){
            return JsonResult.success();
        }else{
            return JsonResult.failure(result);
        }
    }

    @RequestMapping("/relieve")
    @Cms
    public JsonResult relieve(@Valid @RequestBody UidPojo uidPojo){
        if(userPermissionService.relieve(uidPojo.getUid())){
            return JsonResult.success();
        }else{
            return JsonResult.failure("解封失败");
        }

    }

    @RequestMapping("/resetPwd")
    @Cms
    public JsonResult resetPwd(@Valid @RequestBody UidPojo uidPojo){
        if(userPermissionService.resetPwd(uidPojo.getUid())){
            return JsonResult.success();
        }else{
            return JsonResult.failure("重置密码失败");
        }
    }

    @RequestMapping("/appointCms")
    @Root
    public JsonResult appointCms(@Valid @RequestBody UidPojo uidPojo){
        if(userPermissionService.appointCms(uidPojo.getUid())){
            return JsonResult.success();
        }else{
            return JsonResult.failure("任命管理员失败");
        }
    }

    @RequestMapping("/cancelCms")
    @Root
    public JsonResult cancelCms(@Valid @RequestBody UidPojo uidPojo){
        if(userPermissionService.cancelCms(uidPojo.getUid())){
            return JsonResult.success();
        }else{
            return JsonResult.failure("取消管理员失败");
        }
    }


}
