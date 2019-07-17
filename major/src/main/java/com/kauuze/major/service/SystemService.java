package com.kauuze.major.service;

import com.jiwuzao.common.domain.mongo.entity.AppVersion;
import com.jiwuzao.common.domain.mongo.entity.SystemNotice;
import com.kauuze.major.domain.mongo.repository.AppVersionRepository;
import com.kauuze.major.domain.mongo.repository.SystemNoticeRepository;
import com.jiwuzao.common.include.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-07 14:19
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class SystemService {
    @Autowired
    private SystemNoticeRepository systemNoticeRepository;
    @Autowired
    private AppVersionRepository appVersionRepository;

    /**
     * 获取APP版本更新
     * @return
     */
    public List<AppVersion> getUpdate(String currentVersion){
        List<AppVersion> list = new ArrayList<>();
        list.add(appVersionRepository.findByVersion(currentVersion));
        list.add(appVersionRepository.findAll(PageUtil.getMaxOne("createTime")).getContent().get(0));
        return list;
    }

    /**
     * 获取系统公告内容
     * @return
     */
    public SystemNotice systemNotice(){
        List<SystemNotice> systemTips =  systemNoticeRepository.findAll();
        return systemTips.get(0);
    }
}
