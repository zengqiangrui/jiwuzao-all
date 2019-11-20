package com.kauuze.major.config;

import com.jiwuzao.common.config.contain.SpringContext;
import com.kauuze.major.ConfigUtil;
import com.jiwuzao.common.domain.enumType.BackRoleEnum;
import com.jiwuzao.common.domain.enumType.SystemGoodsNameEnum;
import com.jiwuzao.common.domain.mongo.entity.AppVersion;
import com.jiwuzao.common.domain.mongo.entity.Category;
import com.jiwuzao.common.domain.mongo.entity.SystemGoods;
import com.jiwuzao.common.domain.mongo.entity.SystemNotice;
import com.jiwuzao.common.domain.mongo.entity.userBastic.UserToken;
import com.kauuze.major.domain.mongo.repository.*;
import com.jiwuzao.common.domain.mysql.entity.Sms;
import com.kauuze.major.domain.mysql.repository.SmsRepository;
import com.kauuze.major.domain.mysql.repository.UserRepository;
import com.jiwuzao.common.include.DateTimeUtil;
import com.jiwuzao.common.include.StringUtil;
import com.kauuze.major.service.SystemService;
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

        AppVersionRepository appVersionRepository = SpringContext.getBean(AppVersionRepository.class);
        SystemNoticeRepository systemNoticeRepository = SpringContext.getBean(SystemNoticeRepository.class);
        SystemGoodsRepository systemGoodsRepository = SpringContext.getBean(SystemGoodsRepository.class);
        CategoryRepository categoryRepository = SpringContext.getBean(CategoryRepository.class);
//        categoryRepository.save(new Category(1,"[{\"value\":\"special\",\"name\":\"灵物\",\"second:[{value:\"special\",name:\"灵物\",}]},{value:\"food\",name:\"极食\",second:[{value:\"tea\",name:\"茶\",},{value:\"honey\",name:\"蜂蜜\",},{value:\"vinegar\",name:\"醋\",},{value:\"soy\",name:\"酱油\",},{value:\"zongzi\",name:\"粽子\"},{value:\"mooncake\",name:\"月饼\"},{value:\"sugar\",name:\"糖果\"},{value:\"delicacies\",name:\"山珍\"},{value:\"baking\",name:\"手工烘焙\"},{value:\"other\",name:\"其他\"}]},{value:\"clothing\",name:\"服装\",second:[{value:\"man\",name:\"男装\"},{value:\"woman\",name:\"女装\"},{value:\"child\",name:\"童装\"},{value:\"other\",name:\"其他\"}]},{value:\"jewelry\",name:\"极饰\",second:[{value:\"hair\",name:\"发饰\",},{value:\"ear\",name:\"耳饰\",},{value:\"ring\",name:\"戒指\",},{value:\"necklace\",name:\"项链\",},{value:\"bracelet\",name:\"手链手镯\",},{value:\"brooch\",name:\"胸针\"},{value:\"other\",name:\"其他\"}]},{value:\"bags\",name:\"箱包\",second:[{value:\"shoulder\",name:\"单肩包\",},{value:\"backpack\",name:\"双肩包\",},{value:\"clutch\",name:\"手拿包\",},{value:\"hand\",name:\"手提包\",},{value:\"wallet\",name:\"钱包\",},{value:\"other\",name:\"其他\",}]},{value:\"appliance\",name:\"器具\",second:[{value:\"flower\",name:\"花器\",},{value:\"tea\",name:\"茶器\",},{value:\"kettle\",name:\"壶器\",},{value:\"wine\",name:\"酒器\",},{value:\"other\",name:\"其他\",}]},{value:\"gift\",name:\"极礼\",second:[{value:\"festival\",name:\"节日礼\",},{value:\"souvenir\",name:\"伴手礼\",},{value:\"year\",name:\"年礼\",},{value:\"customized\",name:\"极物造定制礼\",},{value:\"other\",name:\"其他\",}]},{value:\"beauty\",name:\"美业\",second:[{value:\"beauty\",name:\"美业\",}]}]",System.currentTimeMillis()));
//        categoryRepository.save(new Category(1, "[{\"value\":\"special\",\"name\":\"灵物\",\"second\":[{\"value\":\"special\",\"name\":\"灵物\"}]},{\"value\":\"food\",\"name\":\"极食\",\"second\":[{\"value\":\"tea\",\"name\":\"茶\"},{\"value\":\"honey\",\"name\":\"蜂蜜\"},{\"value\":\"vinegar\",\"name\":\"醋\"},{\"value\":\"soy\",\"name\":\"酱油\"},{\"value\":\"zongzi\",\"name\":\"粽子\"},{\"value\":\"mooncake\",\"name\":\"月饼\"},{\"value\":\"sugar\",\"name\":\"糖果\"},{\"value\":\"delicacies\",\"name\":\"山珍\"},{\"value\":\"baking\",\"name\":\"手工烘焙\"},{\"value\":\"other\",\"name\":\"其他\"}]},{\"value\":\"clothing\",\"name\":\"服装\",\"second\":[{\"value\":\"man\",\"name\":\"男装\"},{\"value\":\"woman\",\"name\":\"女装\"},{\"value\":\"child\",\"name\":\"童装\"},{\"value\":\"other\",\"name\":\"其他\"}]},{\"value\":\"jewelry\",\"name\":\"极饰\",\"second\":[{\"value\":\"hair\",\"name\":\"发饰\"},{\"value\":\"ear\",\"name\":\"耳饰\"},{\"value\":\"ring\",\"name\":\"戒指\"},{\"value\":\"necklace\",\"name\":\"项链\"},{\"value\":\"bracelet\",\"name\":\"手链手镯\"},{\"value\":\"brooch\",\"name\":\"胸针\"},{\"value\":\"other\",\"name\":\"其他\"}]},{\"value\":\"bags\",\"name\":\"箱包\",\"second\":[{\"value\":\"shoulder\",\"name\":\"单肩包\"},{\"value\":\"backpack\",\"name\":\"双肩包\"},{\"value\":\"clutch\",\"name\":\"手拿包\"},{\"value\":\"hand\",\"name\":\"手提包\"},{\"value\":\"wallet\",\"name\":\"钱包\"},{\"value\":\"other\",\"name\":\"其他\"}]},{\"value\":\"appliance\",\"name\":\"器具\",\"second\":[{\"value\":\"flower\",\"name\":\"花器\"},{\"value\":\"tea\",\"name\":\"茶器\"},{\"value\":\"kettle\",\"name\":\"壶器\"},{\"value\":\"wine\",\"name\":\"酒器\"},{\"value\":\"other\",\"name\":\"其他\"}]},{\"value\":\"gift\",\"name\":\"极礼\",\"second\":[{\"value\":\"festival\",\"name\":\"节日礼\"},{\"value\":\"souvenir\",\"name\":\"伴手礼\"},{\"value\":\"year\",\"name\":\"年礼\"},{\"value\":\"customized\",\"name\":\"极物造定制礼\"},{\"value\":\"other\",\"name\":\"其他\"}]},{\"value\":\"beauty\",\"name\":\"美业\",\"second\":[{\"value\":\"beauty\",\"name\":\"美业\"}]}]", System.currentTimeMillis()));
        categoryRepository.save(new Category(2, "[{\"value\":\"special\",\"name\":\"灵物\",\"second\":[{\"value\":\"foxiang\",\"name\":\"佛像\"},{\"value\":\"tangka\",\"name\":\"唐卡\",\"third\":[{\"value\":\"guotang\",\"name\":\"国唐\"},{\"value\":\"zhitang\",\"name\":\"止唐\"}]},{\"value\":\"xiangdao\",\"name\":\"香道\",\"third\":[{\"value\":\"xiangqi\",\"name\":\"香器\"},{\"value\":\"xianglu\",\"name\":\"香炉\"},{\"value\":\"xiang\",\"name\":\"香\"}]},{\"value\":\"wenwan\",\"name\":\"文玩\",\"third\":[{\"value\":\"fozhu\",\"name\":\"佛珠\"},{\"value\":\"shoucuan\",\"name\":\"手串\"},{\"value\":\"shanyi\",\"name\":\"扇艺\"},{\"value\":\"zhudiao\",\"name\":\"竹雕\"},{\"value\":\"zhuanke\",\"name\":\"篆刻\"},{\"value\":\"mudiao\",\"name\":\"木雕\"},{\"value\":\"shidiao\",\"name\":\"石雕\"}]}]},{\"value\":\"food\",\"name\":\"极食\",\"second\":[{\"value\":\"techan\",\"name\":\"极物特产馆\",\"third\":[{\"value\":\"yunnam\",\"name\":\"云南\"},{\"value\":\"guizhou\",\"name\":\"贵州\"},{\"value\":\"sichuan\",\"name\":\"四川\"},{\"value\":\"xizang\",\"name\":\"西藏\"},{\"value\":\"xinjiang\",\"name\":\"新疆\"},{\"value\":\"hainan\",\"name\":\"海南\"}]},{\"value\":\"chajiukaru\",\"name\":\"茶酒咖乳\",\"third\":[{\"value\":\"wine\",\"name\":\"酒\"},{\"value\":\"tea\",\"name\":\"茶叶\"},{\"value\":\"coffee\",\"name\":\"咖啡\"},{\"value\":\"dairy\",\"name\":\"乳品\"}]},{\"value\":\"gaodianmimian\",\"name\":\"糕点米面\",\"third\":[{\"value\":\"gaodian\",\"name\":\"糕点\"},{\"value\":\"rice\",\"name\":\"米\"},{\"value\":\"noodle\",\"name\":\"面\"},{\"value\":\"snacks\",\"name\":\"零食\"}]},{\"value\":\"sauce\",\"name\":\"粮油酱醋\",\"third\":[{\"value\":\"liangyou\",\"name\":\"粮油\"},{\"value\":\"soy\",\"name\":\"酱油\"},{\"value\":\"vinegar\",\"name\":\"醋\"}]},{\"value\":\"chuantongzibu\",\"name\":\"传统滋补\",\"third\":[{\"value\":\"zibu\",\"name\":\"滋补\"},{\"value\":\"药膳\",\"name\":\"yaoshan\"},{\"value\":\"honey\",\"name\":\"蜂蜜\"}]}]},{\"value\":\"clothing\",\"name\":\"服装\",\"second\":[{\"value\":\"indie\",\"name\":\"独立设计师\",\"third\":[{\"value\":\"nanzhuang\",\"name\":\"男装\"},{\"value\":\"nvzhuang\",\"name\":\"女装\"}]},{\"value\":\"chaopai\",\"name\":\"潮派\"},{\"value\":\"tangzhuang\",\"name\":\"唐装\",\"third\":[{\"value\":\"tangzhuang\",\"name\":\"唐装\"},{\"value\":\"qipao\",\"name\":\"旗袍\"}]},{\"value\":\"minzu\",\"name\":\"民族\",\"third\":[{\"value\":\"hanfu\",\"name\":\"汉服\"},{\"value\":\"minzu\",\"name\":\"民族\"},{\"value\":\"mianma\",\"name\":\"棉麻\"}]},{\"value\":\"tongzhuang\",\"name\":\"童装\"}]},{\"value\":\"jewelry\",\"name\":\"极饰\",\"second\":[{\"value\":\"hair\",\"name\":\"发饰\"},{\"value\":\"ear\",\"name\":\"耳饰\"},{\"value\":\"ring\",\"name\":\"戒指\"},{\"value\":\"necklace\",\"name\":\"项链\"},{\"value\":\"bracelet\",\"name\":\"手链手镯\"},{\"value\":\"brooch\",\"name\":\"胸针\"},{\"value\":\"other\",\"name\":\"其他\"}]},{\"value\":\"bags\",\"name\":\"箱包\",\"second\":[{\"value\":\"shoulder\",\"name\":\"单肩包\"},{\"value\":\"backpack\",\"name\":\"双肩包\"},{\"value\":\"clutch\",\"name\":\"手拿包\"},{\"value\":\"hand\",\"name\":\"手提包\"},{\"value\":\"wallet\",\"name\":\"钱包\"},{\"value\":\"gongwenbao\",\"name\":\"公文包\"},{\"value\":\"diannaobao\",\"name\":\"电脑包\"},{\"value\":\"biaodai\",\"name\":\"定制表带\"}]},{\"value\":\"appliance\",\"name\":\"器具\",\"second\":[{\"value\":\"tea\",\"name\":\"茶器\",\"third\":[{\"value\":\"kettle\",\"name\":\"茶壶\"},{\"value\":\"pinmingbei\",\"name\":\"品茗杯\"},{\"value\":\"gaiwan\",\"name\":\"盖碗\"},{\"value\":\"chachong\",\"name\":\"茶宠\"},{\"value\":\"chapan\",\"name\":\"茶盘\"},{\"value\":\"chayeguan\",\"name\":\"茶叶罐\"},{\"value\":\"faircup\",\"name\":\"公道杯\"},{\"value\":\"beituo\",\"name\":\"杯托\"},{\"value\":\"beidian\",\"name\":\"杯垫\"},{\"value\":\"chajin\",\"name\":\"茶巾\"},{\"value\":\"zhuchaqi\",\"name\":\"煮茶器\"}]},{\"value\":\"home\",\"name\":\"家居\",\"third\":[{\"value\":\"jujiariyong\",\"name\":\"居家日用\"},{\"value\":\"buyi\",\"name\":\"布艺\"},{\"value\":\"ruanzhuang\",\"name\":\"软装\"},{\"value\":\"jiaju\",\"name\":\"家居\"},{\"value\":\"chuangpin\",\"name\":\"床品\"},{\"value\":\"furniture\",\"name\":\"家具\"},{\"value\":\"familyclean\",\"name\":\"家庭清洁\"}]},{\"value\":\"wenfang\",\"name\":\"文房\",\"third\":[{\"value\":\"pen\",\"name\":\"笔\"},{\"value\":\"ink\",\"name\":\"墨\"},{\"value\":\"paper\",\"name\":\"纸\"},{\"value\":\"inkstone\",\"name\":\"砚\"}]},{\"value\":\"kettle\",\"name\":\"壶器\"},{\"value\":\"wine\",\"name\":\"酒器\"},{\"value\":\"other\",\"name\":\"其他\"}]},{\"value\":\"gift\",\"name\":\"极礼\",\"second\":[{\"value\":\"festival\",\"name\":\"节日礼\"},{\"value\":\"souvenir\",\"name\":\"伴手礼\"},{\"value\":\"year\",\"name\":\"年礼\"},{\"value\":\"customized\",\"name\":\"定制礼\"},{\"value\":\"shouzuo\",\"name\":\"手作\"},{\"value\":\"xianliang\",\"name\":\"限量\"},{\"value\":\"nianhuo\",\"name\":\"年货\"},{\"value\":\"mooncake\",\"name\":\"月饼\"},{\"value\":\"zongzi\",\"name\":\"粽子\"},{\"value\":\"other\",\"name\":\"其他\"}]},{\"value\":\"beauty\",\"name\":\"美业\",\"second\":[{\"value\":\"colorful\",\"name\":\"彩妆\"},{\"value\":\"skin\",\"name\":\"护肤\"},{\"value\":\"oralcare\",\"name\":\"口腔护理\"},{\"value\":\"bodycare\",\"name\":\"身体护理\"},{\"value\":\"haircare\",\"name\":\"洗发护发\"},{\"value\":\"香水\",\"name\":\"perfume\"},{\"value\":\"tools\",\"name\":\"美妆工具\"}]}]", System.currentTimeMillis()));

        if (systemGoodsRepository.findAll().size() == 0) {
            SystemGoods systemGoods = new SystemGoods(null, SystemGoodsNameEnum.deposit, SystemGoodsNameEnum.deposit.name, null, new BigDecimal("0.01"));
            systemGoodsRepository.insert(systemGoods);
        }

        //手动修改更新版本
//        if (StringUtil.isEq(ConfigUtil.customEnvironment, "prod")) {
//            SystemService bean = SpringContext.getBean(SystemService.class);
//            AppVersion appVersion = bean.createUpdateVersion("1.0.0", 100, "http://download.jiwuzao.com/jiwuzao.apk", "更新");
//            if(appVersion == null) throw new RuntimeException("请检查版本信息和更新环境");
//        }

        if (appVersionRepository.findAll().size() == 0) {
            AppVersion appVersion = new AppVersion();
            appVersion.setCreateTime(System.currentTimeMillis());
            appVersion.setVersion("1.0.0");
            appVersion.setVersionCode(100);
            appVersion.setUpdateContent("系统初始化版本号");
            appVersion.setDownloadUrl("http://download.jiwuzao.com/jiwuzao.apk");
            appVersion.setStop(false);
            appVersionRepository.insert(appVersion);
        }

//        版本更迭
//        SystemService bean = SpringContext.getBean(SystemService.class);
//        AppVersion appVersion = bean.createUpdateVersion("1.0.8", 108, "http://download.jiwuzao.com/jiwuzao.apk", "内容优化");
//        if (appVersion == null) throw new RuntimeException("请检查版本信息和更新环境");


        if (systemNoticeRepository.findAll().size() == 0) {
            SystemNotice systemNotice = new SystemNotice();
            systemNoticeRepository.insert(systemNotice);
        }

//        UserRepository userRepository = SpringContext.getBean(UserRepository.class);
//        UserTokenRepository userTokenRepository = SpringContext.getBean(UserTokenRepository.class);
//        if (userRepository.findAll().size() == 0) {
//            UserBasicService userBasicService = SpringContext.getBean(UserBasicService.class);
//            Integer msCode = userBasicService.sendSms("10000000000");
//            Integer msCode2 = userBasicService.sendSms("10000000001");
//            Integer msCode3 = userBasicService.sendSms("10000000002");
//            String accessToken = (String) userBasicService.register("10000000000", "qq1501643251", "kauuze", msCode).getData();
//            String accessToken2 = (String) userBasicService.register("10000000001", "qq1501643251", "kauuze2", msCode2).getData();
//            String accessToken3 = (String) userBasicService.register("10000000002", "qq1501643251", "kauuze3", msCode3).getData();
//            UserToken userToken = userTokenRepository.findByAccessToken(accessToken);
//            UserToken userToken2 = userTokenRepository.findByAccessToken(accessToken2);
//            UserToken userToken3 = userTokenRepository.findByAccessToken(accessToken3);
//            userToken.setBackRole(BackRoleEnum.root);
//            userToken2.setBackRole(BackRoleEnum.cms);
//            userToken3.setVip(true);
//            userToken3.setVipEndTime(DateTimeUtil.covertMill(LocalDateTime.now().plusYears(100)));
//            userTokenRepository.save(userToken);
//            userTokenRepository.save(userToken2);
//            userTokenRepository.save(userToken3);
//            userTokenRepository.save(userToken3);
//            SmsRepository smsRepository = SpringContext.getBean(SmsRepository.class);
//            Sms sms = smsRepository.findByPhone("10000000000");
//            Sms sms2 = smsRepository.findByPhone("10000000001");
//            Sms sms3 = smsRepository.findByPhone("10000000002");
//            sms.setCode(123123);
//            sms.setOverTime(2111111111111L);
//            smsRepository.save(sms);
//            sms2.setCode(123123);
//            sms2.setOverTime(2111111111111L);
//            smsRepository.save(sms2);
//            sms3.setCode(123123);
//            sms3.setOverTime(2111111111111L);
//            smsRepository.save(sms3);
//        }
    }

}
