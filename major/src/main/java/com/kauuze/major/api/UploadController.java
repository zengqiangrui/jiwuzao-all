package com.kauuze.major.api;


import com.jiwuzao.common.include.yun.QiniuUtil;
import com.jiwuzao.common.pojo.common.SuffixPojo;
import com.jiwuzao.common.pojo.common.UrlPojo;
import com.jiwuzao.common.pojo.common.UrlsPojo;
import com.jiwuzao.common.include.JsonResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-17 15:06
 */
@RestController
@RequestMapping("/upload")
public class UploadController {
    @RequestMapping("/upToken")
    public JsonResult upToken(@Valid @RequestBody SuffixPojo suffixPojo) {
        return JsonResult.success(QiniuUtil.upToken(suffixPojo.getSuffix()));
    }

    @RequestMapping("/delFilesBatch")
    public JsonResult delFilesBatch(@Valid @RequestBody UrlsPojo urlsPojo) {
        QiniuUtil.delFilesBatch(urlsPojo.getUrls());
        return JsonResult.success();
    }

    @RequestMapping("/delSingle")
    public JsonResult delSingle(@Valid @RequestBody UrlPojo urlPojo) {
        if (QiniuUtil.delSingle(urlPojo.getUrl()))
            return JsonResult.success();
        return JsonResult.failure();
    }
}
