package com.kauuze.manager.api;

import com.jiwuzao.common.pojo.common.NamePojo;
import com.jiwuzao.common.pojo.common.PagePojo;
import com.jiwuzao.common.pojo.common.UidPojo;
import com.kauuze.manager.api.pojo.storeView.StoreForbidPojo;
import com.kauuze.manager.config.permission.Cms;
import com.kauuze.manager.include.JsonResult;
import com.kauuze.manager.include.PageDto;
import com.kauuze.manager.service.StoreViewService;
import com.kauuze.manager.service.dto.storeView.StoreShowDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/storeView")
public class StoreViewController {
    @Autowired
    private StoreViewService storeViewService;

    @RequestMapping("/findByUid")
    @Cms
    public JsonResult findByUid(@Valid @RequestBody UidPojo uidPojo) {
        return JsonResult.success(storeViewService.findByUid(uidPojo.getUid()));
    }

    @RequestMapping("/findAll")
    @Cms
    public JsonResult findAll(@Valid @RequestBody PagePojo pojo){
        PageDto<StoreShowDto> pageDto = storeViewService.findAllStorePage(pojo.getNum(), pojo.getSize());
        return JsonResult.success(pageDto);
    }

    @RequestMapping("/findByStoreName")
    @Cms
    public JsonResult findByStoreName(@Valid @RequestBody NamePojo namePojo) {
        return JsonResult.success(storeViewService.findByStoreName(namePojo.getName()));
    }

    @RequestMapping("/illegalStore")
    @Cms
    public JsonResult illegalStore(@Valid @RequestBody StoreForbidPojo storeForbidPojo){
        if(storeViewService.illegalStore(storeForbidPojo.getUid(), storeForbidPojo.getViolationCause())){
            return JsonResult.success();
        }else{
            return JsonResult.failure();
        }
    }
}
