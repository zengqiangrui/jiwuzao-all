package com.kauuze.major.config;

import com.kauuze.major.ConfigUtil;
import com.kauuze.major.config.contain.SpringContext;
import com.kauuze.major.domain.enumType.BackRoleEnum;
import com.kauuze.major.domain.enumType.SystemGoodsNameEnum;
import com.kauuze.major.domain.mongo.entity.AppVersion;
import com.kauuze.major.domain.mongo.entity.Category;
import com.kauuze.major.domain.mongo.entity.SystemGoods;
import com.kauuze.major.domain.mongo.entity.SystemNotice;
import com.kauuze.major.domain.mongo.entity.userBastic.UserToken;
import com.kauuze.major.domain.mongo.repository.*;
import com.kauuze.major.domain.mysql.entity.Sms;
import com.kauuze.major.domain.mysql.repository.SmsRepository;
import com.kauuze.major.domain.mysql.repository.UserRepository;
import com.kauuze.major.include.DateTimeUtil;
import com.kauuze.major.include.StringUtil;
import com.kauuze.major.service.UserBasicService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * springboot 开机执行
 */
@Component
@Order(1)
public class ApplicationRunnerAfter implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        if (!ConfigUtil.validCustomEnvironment()) {
            throw new RuntimeException("validCustomEnvironment error!");
        }
        if (!StringUtil.isEq(ConfigUtil.customEnvironment, "dev")) {
            return;
        }
        AppVersionRepository appVersionRepository = SpringContext.getBean(AppVersionRepository.class);
        SystemNoticeRepository systemNoticeRepository = SpringContext.getBean(SystemNoticeRepository.class);
        SystemGoodsRepository systemGoodsRepository = SpringContext.getBean(SystemGoodsRepository.class);
        CategoryRepository categoryRepository = SpringContext.getBean(CategoryRepository.class);
//        categoryRepository.save(new Category(1,"[{\"value\":\"special\",\"name\":\"灵物\",\"second:[{value:\"special\",name:\"灵物\",}]},{value:\"food\",name:\"极食\",second:[{value:\"tea\",name:\"茶\",},{value:\"honey\",name:\"蜂蜜\",},{value:\"vinegar\",name:\"醋\",},{value:\"soy\",name:\"酱油\",},{value:\"zongzi\",name:\"粽子\"},{value:\"mooncake\",name:\"月饼\"},{value:\"sugar\",name:\"糖果\"},{value:\"delicacies\",name:\"山珍\"},{value:\"baking\",name:\"手工烘焙\"},{value:\"other\",name:\"其他\"}]},{value:\"clothing\",name:\"服装\",second:[{value:\"man\",name:\"男装\"},{value:\"woman\",name:\"女装\"},{value:\"child\",name:\"童装\"},{value:\"other\",name:\"其他\"}]},{value:\"jewelry\",name:\"极饰\",second:[{value:\"hair\",name:\"发饰\",},{value:\"ear\",name:\"耳饰\",},{value:\"ring\",name:\"戒指\",},{value:\"necklace\",name:\"项链\",},{value:\"bracelet\",name:\"手链手镯\",},{value:\"brooch\",name:\"胸针\"},{value:\"other\",name:\"其他\"}]},{value:\"bags\",name:\"箱包\",second:[{value:\"shoulder\",name:\"单肩包\",},{value:\"backpack\",name:\"双肩包\",},{value:\"clutch\",name:\"手拿包\",},{value:\"hand\",name:\"手提包\",},{value:\"wallet\",name:\"钱包\",},{value:\"other\",name:\"其他\",}]},{value:\"appliance\",name:\"器具\",second:[{value:\"flower\",name:\"花器\",},{value:\"tea\",name:\"茶器\",},{value:\"kettle\",name:\"壶器\",},{value:\"wine\",name:\"酒器\",},{value:\"other\",name:\"其他\",}]},{value:\"gift\",name:\"极礼\",second:[{value:\"festival\",name:\"节日礼\",},{value:\"souvenir\",name:\"伴手礼\",},{value:\"year\",name:\"年礼\",},{value:\"customized\",name:\"极物造定制礼\",},{value:\"other\",name:\"其他\",}]},{value:\"beauty\",name:\"美业\",second:[{value:\"beauty\",name:\"美业\",}]}]",System.currentTimeMillis()));
        categoryRepository.save(new Category(1, "[{\"value\":\"special\",\"name\":\"灵物\",\"second\":[{\"value\":\"special\",\"name\":\"灵物\"}]},{\"value\":\"food\",\"name\":\"极食\",\"second\":[{\"value\":\"tea\",\"name\":\"茶\"},{\"value\":\"honey\",\"name\":\"蜂蜜\"},{\"value\":\"vinegar\",\"name\":\"醋\"},{\"value\":\"soy\",\"name\":\"酱油\"},{\"value\":\"zongzi\",\"name\":\"粽子\"},{\"value\":\"mooncake\",\"name\":\"月饼\"},{\"value\":\"sugar\",\"name\":\"糖果\"},{\"value\":\"delicacies\",\"name\":\"山珍\"},{\"value\":\"baking\",\"name\":\"手工烘焙\"},{\"value\":\"other\",\"name\":\"其他\"}]},{\"value\":\"clothing\",\"name\":\"服装\",\"second\":[{\"value\":\"man\",\"name\":\"男装\"},{\"value\":\"woman\",\"name\":\"女装\"},{\"value\":\"child\",\"name\":\"童装\"},{\"value\":\"other\",\"name\":\"其他\"}]},{\"value\":\"jewelry\",\"name\":\"极饰\",\"second\":[{\"value\":\"hair\",\"name\":\"发饰\"},{\"value\":\"ear\",\"name\":\"耳饰\"},{\"value\":\"ring\",\"name\":\"戒指\"},{\"value\":\"necklace\",\"name\":\"项链\"},{\"value\":\"bracelet\",\"name\":\"手链手镯\"},{\"value\":\"brooch\",\"name\":\"胸针\"},{\"value\":\"other\",\"name\":\"其他\"}]},{\"value\":\"bags\",\"name\":\"箱包\",\"second\":[{\"value\":\"shoulder\",\"name\":\"单肩包\"},{\"value\":\"backpack\",\"name\":\"双肩包\"},{\"value\":\"clutch\",\"name\":\"手拿包\"},{\"value\":\"hand\",\"name\":\"手提包\"},{\"value\":\"wallet\",\"name\":\"钱包\"},{\"value\":\"other\",\"name\":\"其他\"}]},{\"value\":\"appliance\",\"name\":\"器具\",\"second\":[{\"value\":\"flower\",\"name\":\"花器\"},{\"value\":\"tea\",\"name\":\"茶器\"},{\"value\":\"kettle\",\"name\":\"壶器\"},{\"value\":\"wine\",\"name\":\"酒器\"},{\"value\":\"other\",\"name\":\"其他\"}]},{\"value\":\"gift\",\"name\":\"极礼\",\"second\":[{\"value\":\"festival\",\"name\":\"节日礼\"},{\"value\":\"souvenir\",\"name\":\"伴手礼\"},{\"value\":\"year\",\"name\":\"年礼\"},{\"value\":\"customized\",\"name\":\"极物造定制礼\"},{\"value\":\"other\",\"name\":\"其他\"}]},{\"value\":\"beauty\",\"name\":\"美业\",\"second\":[{\"value\":\"beauty\",\"name\":\"美业\"}]}]", System.currentTimeMillis()));

        if (systemGoodsRepository.findAll().size() == 0) {
            SystemGoods systemGoods = new SystemGoods(null, SystemGoodsNameEnum.deposit, SystemGoodsNameEnum.deposit.name, null, new BigDecimal("0.01"));
            systemGoodsRepository.insert(systemGoods);
        }
        if (appVersionRepository.findAll().size() == 0) {
            AppVersion appVersion = new AppVersion();
            appVersion.setCreateTime(System.currentTimeMillis());
            appVersion.setVersion("1.0.0");
            appVersion.setUpdateContent("系统初始化版本号");
            appVersion.setStop(false);
            appVersionRepository.insert(appVersion);
        }
        if (systemNoticeRepository.findAll().size() == 0) {
            SystemNotice systemNotice = new SystemNotice();
            systemNoticeRepository.insert(systemNotice);
        }
        UserRepository userRepository = SpringContext.getBean(UserRepository.class);
        UserTokenRepository userTokenRepository = SpringContext.getBean(UserTokenRepository.class);
        if (userRepository.findAll().size() == 0) {
            UserBasicService userBasicService = SpringContext.getBean(UserBasicService.class);
            Integer msCode = userBasicService.sendSms("10000000000");
            Integer msCode2 = userBasicService.sendSms("10000000001");
            Integer msCode3 = userBasicService.sendSms("10000000002");
            String accessToken = (String) userBasicService.register("10000000000", "qq1501643251", "kauuze", msCode).getData();
            String accessToken2 = (String) userBasicService.register("10000000001", "qq1501643251", "kauuze2", msCode2).getData();
            String accessToken3 = (String) userBasicService.register("10000000002", "qq1501643251", "kauuze3", msCode3).getData();
            UserToken userToken = userTokenRepository.findByAccessToken(accessToken);
            UserToken userToken2 = userTokenRepository.findByAccessToken(accessToken2);
            UserToken userToken3 = userTokenRepository.findByAccessToken(accessToken3);
            userToken.setBackRole(BackRoleEnum.root);
            userToken2.setBackRole(BackRoleEnum.cms);
            userToken3.setVip(true);
            userToken3.setVipEndTime(DateTimeUtil.covertMill(LocalDateTime.now().plusYears(100)));
            userTokenRepository.save(userToken);
            userTokenRepository.save(userToken2);
            userTokenRepository.save(userToken3);
            userTokenRepository.save(userToken3);
            SmsRepository smsRepository = SpringContext.getBean(SmsRepository.class);
            Sms sms = smsRepository.findByPhone("10000000000");
            Sms sms2 = smsRepository.findByPhone("10000000001");
            Sms sms3 = smsRepository.findByPhone("10000000002");
            sms.setCode(123123);
            sms.setOverTime(2111111111111L);
            smsRepository.save(sms);
            sms2.setCode(123123);
            sms2.setOverTime(2111111111111L);
            smsRepository.save(sms2);
            sms3.setCode(123123);
            sms3.setOverTime(2111111111111L);
            smsRepository.save(sms3);
        }
    }
}