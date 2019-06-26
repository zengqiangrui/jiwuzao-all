package com.kauuze.manager;

import com.kauuze.manager.config.permission.GreenWay;
import com.kauuze.manager.include.JsonResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-25 16:47
 */
@RequestMapping("/test")
@RestController
public class TestController {
    @RequestMapping("/test")
    @GreenWay
    public JsonResult test(){
        return JsonResult.success();
    }

    @RequestMapping("/test2")
    @GreenWay
    public JsonResult test2(){
        return JsonResult.success();
    }

    @RequestMapping("/mill")
    @GreenWay
    public JsonResult mill(){
        return JsonResult.success(System.currentTimeMillis());
    }
}
