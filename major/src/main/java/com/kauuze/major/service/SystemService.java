package com.kauuze.major.service;

import com.jiwuzao.common.domain.mongo.entity.AppVersion;
import com.jiwuzao.common.domain.mongo.entity.SystemNotice;
import com.jiwuzao.common.include.StringUtil;
import com.kauuze.major.domain.mongo.repository.AppVersionRepository;
import com.kauuze.major.domain.mongo.repository.SystemNoticeRepository;
import com.jiwuzao.common.include.PageUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
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
     *
     * @return
     */
    public List<AppVersion> getUpdate(String currentVersion) {
        List<AppVersion> list = new ArrayList<>();
        list.add(appVersionRepository.findByVersion(currentVersion));
        list.add(appVersionRepository.findAll(PageUtil.getMaxOne("createTime")).getContent().get(0));
        return list;
    }

    public AppVersion createUpdateVersion(String version, Integer versionCode, String downloadUrl, String updateMsg) {
        if (!StringUtil.checkVersion(version, versionCode)) throw new RuntimeException("版本信息输入有误！");
        AppVersion lastVersion = appVersionRepository.findAll(PageUtil.getMaxOne("createTime")).getContent().get(0);
        if (versionCode <= lastVersion.getVersionCode()) {
            throw new RuntimeException("版本应比原版本高");
        }
        return appVersionRepository.save(new AppVersion().setStop(false).setCreateTime(System.currentTimeMillis())
                .setUpdateContent(updateMsg).setDownloadUrl(downloadUrl).setVersion(version).setVersionCode(versionCode)
        );
    }

    /**
     * 获取系统公告内容
     *
     * @return
     */
    public SystemNotice systemNotice() {
        List<SystemNotice> systemTips = systemNoticeRepository.findAll();
        return systemTips.get(0);
    }
}
