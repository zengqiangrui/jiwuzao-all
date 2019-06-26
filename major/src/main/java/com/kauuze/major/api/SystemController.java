package com.kauuze.major.api;


import com.kauuze.major.api.pojo.system.AppVersionPojo;
import com.kauuze.major.include.JsonResult;
import com.kauuze.major.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-07 14:17
 */
@RestController
@RequestMapping("/system")
public class SystemController {
    @Autowired
    private SystemService systemService;
    @RequestMapping("/appVersion")
    public JsonResult appVersion(@Valid @RequestBody AppVersionPojo appVersionPojo){
        return JsonResult.success(systemService.getUpdate(appVersionPojo.getCurrentVersion()));
    }

    @RequestMapping("/systemNotice")
    public JsonResult systemNotice(){
        return JsonResult.success(systemService.systemNotice());
    }
}
