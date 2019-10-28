package com.kauuze.manager.api;

import com.jiwuzao.common.domain.mysql.entity.ReturnOrder;
import com.jiwuzao.common.pojo.common.PagePojo;
import com.kauuze.manager.config.permission.Cms;
import com.kauuze.manager.include.JsonResult;
import com.kauuze.manager.include.PageDto;
import com.kauuze.manager.service.ReturnOrderService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/return")
public class ReturnOrderController {
    @Resource
    private ReturnOrderService returnOrderService;

    @RequestMapping("/getAllPage")
    @Cms
    public JsonResult getAll(@Valid @RequestBody PagePojo pojo) {
        return JsonResult.success(returnOrderService.getReturnOrderVOS(pojo.getNum(), pojo.getSize()));
    }


}
