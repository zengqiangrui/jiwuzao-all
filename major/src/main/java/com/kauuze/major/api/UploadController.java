package com.kauuze.major.api;


import com.jiwuzao.common.dto.others.LayImgDto;
import com.jiwuzao.common.dto.others.LayUploadDto;
import com.jiwuzao.common.include.JsonUtil;
import com.jiwuzao.common.include.Rand;
import com.jiwuzao.common.include.yun.QiniuUtil;
import com.jiwuzao.common.pojo.common.SuffixPojo;
import com.jiwuzao.common.pojo.common.UrlPojo;
import com.jiwuzao.common.pojo.common.UrlsPojo;
import com.jiwuzao.common.include.JsonResult;
import com.kauuze.major.config.permission.Authorization;
import com.qiniu.util.Json;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-17 15:06
 */
@RestController
@RequestMapping("/upload")
@Slf4j
public class UploadController {
    @RequestMapping("/upToken")
    public JsonResult upToken(@Valid @RequestBody SuffixPojo suffixPojo) {
        return JsonResult.success(QiniuUtil.upToken(suffixPojo.getSuffix()));
    }

    @RequestMapping("/richUpToken")
    public JsonResult richUpToken() {
        return JsonResult.success(QiniuUtil.upToken(""));
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

    @RequestMapping("/myUpload")
    @Authorization
    public String doUpload(@RequestAttribute int uid, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("文件为空");
        }
        if (file.getSize() > 1024 * 1024 * 5)
            throw new RuntimeException("文件大小应小于5m");
//        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());//根据日期上传图片进行保存
        String filePath = "/root/jiwuzao/images/";
        File f = new File(filePath);
        if (!f.exists()) f.mkdirs();
        String fName = Rand.getUUID() + file.getOriginalFilename();
        File dest = new File(filePath + "/" + fName);
        LayUploadDto layUploadDto = new LayUploadDto();
        try {
            file.transferTo(dest);
            layUploadDto.setCode(0).setMsg("上传成功").setData(new LayImgDto()
                    .setSrc("http://api.jiwuzao.com/jiwuzao/" + fName).setTitle(file.getOriginalFilename()));
            log.info("上传成功");
            return JsonUtil.toJsonString(layUploadDto);
        } catch (IOException e) {
            log.error(e.toString(), e);
        }
        layUploadDto.setCode(-1).setMsg("上传失败").setData(null);
        return JsonUtil.toJsonString(layUploadDto);
    }

}