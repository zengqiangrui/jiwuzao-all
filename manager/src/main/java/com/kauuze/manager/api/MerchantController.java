package com.kauuze.manager.api;

import com.jiwuzao.common.domain.mongo.entity.userBastic.VerifyActor;
import com.jiwuzao.common.pojo.common.AuditTypePojo;
import com.jiwuzao.common.pojo.common.PagePojo;
import com.jiwuzao.common.pojo.common.UidPojo;
import com.kauuze.manager.api.pojo.userPermission.AuditVerifyActorPojo;
import com.kauuze.manager.config.permission.Cms;
import com.kauuze.manager.include.JsonResult;
import com.kauuze.manager.include.PageDto;
import com.kauuze.manager.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-06-10 13:51
 */
@RestController
@RequestMapping("/merchant")
public class MerchantController {
    @Autowired
    private MerchantService merchantService;

    /**
     * 按认证状态分页查询
     * @param auditTypePojo
     * @return
     */
    @RequestMapping("/findVerifyActorByAuditType")
    @Cms
    public JsonResult findVerifyActorByAuditType(@Valid @RequestBody AuditTypePojo auditTypePojo){
        PagePojo pagePojo = auditTypePojo.getPage();
        PageDto<VerifyActor> pageDto = merchantService.findVerifyActorByAuditType(auditTypePojo.getAuditType(),
                pagePojo.getNum(), 20);
        return JsonResult.success(pageDto);
    }

    /**
     * 按uid查询认证信息
     * @param uidPojo
     * @return
     */
    @RequestMapping("/findVerifyActorByUid")
    @Cms
    public JsonResult findVerifyActor(@Valid @RequestBody UidPojo uidPojo) {
        return JsonResult.success(merchantService.findVerifyActorByUid(uidPojo.getUid()));
    }

    /**
     * 改变认证状态
     * @param auditVerifyActorPojo
     * @return
     */
    @RequestMapping("/auditVerifyActor")
    @Cms
    public JsonResult auditVerifyActor(@Valid @RequestBody AuditVerifyActorPojo auditVerifyActorPojo){
        String result = merchantService.auditVerifyActor(auditVerifyActorPojo.getUid(),auditVerifyActorPojo.getAuditType(),auditVerifyActorPojo.getRefuseReason());
        if(result == null){
            return JsonResult.success();
        }else {
            return JsonResult.failure(result);
        }
    }


}
