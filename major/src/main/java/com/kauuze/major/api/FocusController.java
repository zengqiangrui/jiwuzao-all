package com.kauuze.major.api;

import com.jiwuzao.common.domain.mongo.entity.Focus;
import com.jiwuzao.common.include.JsonResult;
import com.jiwuzao.common.pojo.common.UidPojo;
import com.jiwuzao.common.vo.user.FocusVO;
import com.kauuze.major.config.permission.Authorization;
import com.kauuze.major.service.FocusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/focus")
public class FocusController {

    @Autowired
    private FocusService focusService;

    @RequestMapping("/create")
    @Authorization
    public JsonResult createOne(@RequestAttribute int uid, @Valid @RequestBody UidPojo uidPojo) {
        Focus focus = focusService.createFocus(uid, uidPojo.getUid());
        if (null != focus) {
            return JsonResult.success(focus);
        } else {
            return JsonResult.failure();
        }
    }

    @RequestMapping("/cancel")
    @Authorization
    public JsonResult cancelOne(@RequestAttribute int uid, @Valid @RequestBody UidPojo uidPojo) {
        Focus focus = focusService.cancelFocus(uid, uidPojo.getUid());
        if (null != focus) {
            return JsonResult.success(focus);
        } else {
            return JsonResult.failure();
        }
    }

    @RequestMapping("/get")
    @Authorization
    public JsonResult getOne(@RequestAttribute int uid, @Valid @RequestBody UidPojo uidPojo) {
        Focus focus = focusService.getFocus(uid, uidPojo.getUid());
        if (null != focus) {
            return JsonResult.success(focus);
        } else {
            return JsonResult.failure();
        }
    }

    @RequestMapping("/getFocusList")
    @Authorization
    public JsonResult getFocusList(@RequestAttribute int uid){
        List<FocusVO> list = focusService.getFocusList(uid);
        return JsonResult.success(list);
    }

    @RequestMapping("/getFansList")
    @Authorization
    public JsonResult getFansList(@RequestAttribute int uid){
        List<FocusVO> list = focusService.getFansList(uid);
        return JsonResult.success(list);
    }

}
