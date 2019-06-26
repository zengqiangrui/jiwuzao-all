package com.kauuze.major;

import com.kauuze.major.config.permission.GreenWay;
import com.kauuze.major.include.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注意删除
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-25 16:47
 */
@RequestMapping("/test")
@RestController
public class TestController {
    @Autowired
    private TestService testService;
    @RequestMapping("/test")
    @GreenWay
    public JsonResult test(){
        testService.test();
        return JsonResult.success();
    }

}
