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
        categoryRepository.save(new Category(3, "[{\"value\":\"food\",\"name\":\"极食\",\"second\":[{\"value\":\"techan\",\"name\":\"极物特产馆\",\"third\":[{\"value\":\"yunnam\",\"name\":\"云南\",\"url\":\"http://cdn.jiwuzao.com/image/category/yunnan.png\"},{\"value\":\"guizhou\",\"name\":\"贵州\",\"url\":\"http://cdn.jiwuzao.com/image/category/guizhou.png\"},{\"value\":\"sichuan\",\"name\":\"四川\",\"url\":\"http://cdn.jiwuzao.com/image/category/sichuan.png\"},{\"value\":\"xizang\",\"name\":\"西藏\",\"url\":\"http://cdn.jiwuzao.com/image/category/xizang.png\"},{\"value\":\"xinjiang\",\"name\":\"新疆\",\"url\":\"http://cdn.jiwuzao.com/image/category/xinjiang.png\"},{\"value\":\"hainan\",\"name\":\"海南\",\"url\":\"http://cdn.jiwuzao.com/image/category/hainan.png\"}]},{\"value\":\"chajiukaru\",\"name\":\"茶酒咖乳\",\"third\":[{\"value\":\"wine\",\"name\":\"酒\",\"url\":\"http://cdn.jiwuzao.com/image/category/wine.png\"},{\"value\":\"tea\",\"name\":\"茶叶\",\"url\":\"http://cdn.jiwuzao.com/image/category/tea.png\"},{\"value\":\"coffee\",\"name\":\"咖啡\",\"url\":\"http://cdn.jiwuzao.com/image/category/coffee.png\"},{\"value\":\"dairy\",\"name\":\"乳品\",\"url\":\"http://cdn.jiwuzao.com/image/category/dairy.png\"}]},{\"value\":\"gaodianmimian\",\"name\":\"糕点米面\",\"third\":[{\"value\":\"gaodian\",\"name\":\"糕点\",\"url\":\"http://cdn.jiwuzao.com/image/category/gaodian.png\"},{\"value\":\"rice\",\"name\":\"米\",\"url\":\"http://cdn.jiwuzao.com/image/category/rice.png\"},{\"value\":\"noodle\",\"name\":\"面\",\"url\":\"http://cdn.jiwuzao.com/image/category/noodle.png\"},{\"value\":\"snacks\",\"name\":\"零食\",\"url\":\"http://cdn.jiwuzao.com/image/category/snacks.png\"}]},{\"value\":\"sauce\",\"name\":\"粮油酱醋\",\"third\":[{\"value\":\"liangyou\",\"name\":\"粮油\",\"url\":\"http://cdn.jiwuzao.com/image/category/liangyou.png\"},{\"value\":\"soy\",\"name\":\"酱油\",\"url\":\"http://cdn.jiwuzao.com/image/category/jiangyou.png\"},{\"value\":\"vinegar\",\"name\":\"醋\",\"url\":\"http://cdn.jiwuzao.com/image/category/cu.png\"}]},{\"value\":\"chuantongzibu\",\"name\":\"传统滋补\",\"third\":[{\"value\":\"zibu\",\"name\":\"滋补\",\"url\":\"http://cdn.jiwuzao.com/image/category/zibu.png\"},{\"value\":\"yaoshan\",\"name\":\"药膳\",\"url\":\"http://cdn.jiwuzao.com/image/category/yaoshan.png\"},{\"value\":\"honey\",\"name\":\"蜂蜜\",\"url\":\"http://cdn.jiwuzao.com/image/category/honey.png\"}]}]},{\"value\":\"appliance\",\"name\":\"器具\",\"second\":[{\"value\":\"tea\",\"name\":\"茶器\",\"third\":[{\"value\":\"kettle\",\"name\":\"茶壶\",\"url\":\"http://cdn.jiwuzao.com/image/category/chahu.png\"},{\"value\":\"pinmingbei\",\"name\":\"品茗杯\",\"url\":\"http://cdn.jiwuzao.com/image/category/pinmingbei.png\"},{\"value\":\"gaiwan\",\"name\":\"盖碗\",\"url\":\"http://cdn.jiwuzao.com/image/category/gaiwan.png\"},{\"value\":\"chapan\",\"name\":\"茶盘\",\"url\":\"http://cdn.jiwuzao.com/image/category/chapan.png\"},{\"value\":\"chayeguan\",\"name\":\"茶叶罐\",\"url\":\"http://cdn.jiwuzao.com/image/category/chayeguan.png\"},{\"value\":\"faircup\",\"name\":\"公道杯\",\"url\":\"http://cdn.jiwuzao.com/image/category/gongdaobei.png\"},{\"value\":\"beituo\",\"name\":\"杯托\",\"url\":\"http://cdn.jiwuzao.com/image/category/beituo.png\"},{\"value\":\"beidian\",\"name\":\"杯垫\",\"url\":\"http://cdn.jiwuzao.com/image/category/beidian.png\"},{\"value\":\"chajin\",\"name\":\"茶巾\",\"url\":\"http://cdn.jiwuzao.com/image/category/chajin.png\"},{\"value\":\"zhuchaqi\",\"name\":\"煮茶器\",\"url\":\"http://cdn.jiwuzao.com/image/category/zhuchaqi.png\"}]},{\"value\":\"wenfang\",\"name\":\"文房\",\"third\":[{\"value\":\"pen\",\"name\":\"笔\",\"url\":\"http://cdn.jiwuzao.com/image/category/bi.png\"},{\"value\":\"ink\",\"name\":\"墨\",\"url\":\"http://cdn.jiwuzao.com/image/category/mo.png\"},{\"value\":\"paper\",\"name\":\"纸\",\"url\":\"http://cdn.jiwuzao.com/image/category/zhi.png\"},{\"value\":\"inkstone\",\"name\":\"砚\",\"url\":\"http://cdn.jiwuzao.com/image/category/yan.png\"}]},{\"value\":\"home\",\"name\":\"家居\",\"third\":[{\"value\":\"jujiariyong\",\"name\":\"居家日用\",\"url\":\"http://cdn.jiwuzao.com/image/category/jujiariyong.png\"},{\"value\":\"buyi\",\"name\":\"布艺\",\"url\":\"http://cdn.jiwuzao.com/image/category/buyi.png\"},{\"value\":\"chuangpin\",\"name\":\"床品\",\"url\":\"http://cdn.jiwuzao.com/image/category/chuangpin.png\"},{\"value\":\"furniture\",\"name\":\"家具\",\"url\":\"http://cdn.jiwuzao.com/image/category/furniture.png\"}]}]},{\"value\":\"gift\",\"name\":\"极礼\",\"url\":\"\",\"second\":[{\"value\":\"gift\",\"name\":\"极礼\",\"third\":[{\"value\":\"customized\",\"name\":\"定制礼\",\"url\":\"http://cdn.jiwuzao.com/image/category/dingzhi.png\"},{\"value\":\"shouzuo\",\"name\":\"手作\",\"url\":\"http://cdn.jiwuzao.com/image/category/shouzuo.png\"},{\"value\":\"xianliang\",\"name\":\"限量\",\"url\":\"http://cdn.jiwuzao.com/image/category/xianliang.png\"},{\"value\":\"nianhuo\",\"name\":\"年货\",\"url\":\"http://cdn.jiwuzao.com/image/category/nianhuo.png\"},{\"value\":\"mooncake\",\"name\":\"月饼\",\"url\":\"http://cdn.jiwuzao.com/image/category/yuebing.png\"},{\"value\":\"zongzi\",\"name\":\"粽子\",\"url\":\"http://cdn.jiwuzao.com/image/category/zongzi.png\"}]}]},{\"value\":\"bags\",\"name\":\"箱包\",\"url\":\"\",\"second\":[{\"value\":\"bags\",\"name\":\"箱包\",\"url\":\"\",\"third\":[{\"value\":\"beibao\",\"name\":\"背包\",\"url\":\"http://cdn.jiwuzao.com/image/category/beibao.png\"},{\"value\":\"kuabao\",\"name\":\"挎包\",\"url\":\"http://cdn.jiwuzao.com/image/category/kuabao.png\"},{\"value\":\"hand\",\"name\":\"手提包\",\"url\":\"http://cdn.jiwuzao.com/image/category/shoutibao.png\"},{\"value\":\"wallet\",\"name\":\"钱包\",\"url\":\"http://cdn.jiwuzao.com/image/category/qianbao.png\"},{\"value\":\"gongwenbao\",\"name\":\"公文包\",\"url\":\"http://cdn.jiwuzao.com/image/category/gongwenbao.png\"},{\"value\":\"diannaobao\",\"name\":\"电脑包\",\"url\":\"http://cdn.jiwuzao.com/image/category/diannaobao.png\"},{\"value\":\"biaodai\",\"name\":\"定制表带\",\"url\":\"http://cdn.jiwuzao.com/image/category/dingzhibiaodai.png\"}]}]},{\"value\":\"jewelry\",\"name\":\"极饰\",\"second\":[{\"value\":\"jewelry\",\"name\":\"极饰\",\"url\":\"\",\"third\":[{\"value\":\"ear\",\"name\":\"耳饰\",\"url\":\"http://cdn.jiwuzao.com/image/category/ershi.png\"},{\"value\":\"ring\",\"name\":\"戒指\",\"url\":\"http://cdn.jiwuzao.com/image/category/jiezhi.png\"},{\"value\":\"necklace\",\"name\":\"项链\",\"url\":\"http://cdn.jiwuzao.com/image/category/xianglian.png\"},{\"value\":\"bracelet\",\"name\":\"手链手镯\",\"url\":\"http://cdn.jiwuzao.com/image/category/shoulian.png\"},{\"value\":\"brooch\",\"name\":\"胸针\",\"url\":\"http://cdn.jiwuzao.com/image/category/xiongzhen.png\"},{\"value\":\"peishi\",\"name\":\"配饰\",\"url\":\"http://cdn.jiwuzao.com/image/category/peishi.png\"}]}]},{\"value\":\"clothing\",\"name\":\"服装\",\"second\":[{\"value\":\"indie\",\"name\":\"独立设计师\",\"third\":[{\"value\":\"nanzhuang\",\"name\":\"男装\",\"url\":\"http://cdn.jiwuzao.com/image/category/nanzhuang.png\"},{\"value\":\"nvzhuang\",\"name\":\"女装\",\"url\":\"http://cdn.jiwuzao.com/image/category/nvzhuang.png\"}]},{\"value\":\"chaopai\",\"name\":\"潮派\",\"url\":\"http://cdn.jiwuzao.com/image/category/chaopai.png\"},{\"value\":\"tangzhuang\",\"name\":\"唐装\",\"third\":[{\"value\":\"tangzhuang\",\"name\":\"唐装\",\"url\":\"http://cdn.jiwuzao.com/image/category/tangzhuang.png\"},{\"value\":\"qipao\",\"name\":\"旗袍\",\"url\":\"http://cdn.jiwuzao.com/image/category/qipao.png\"}]},{\"value\":\"minzu\",\"name\":\"民族\",\"third\":[{\"value\":\"hanfu\",\"name\":\"汉服\",\"url\":\"http://cdn.jiwuzao.com/image/category/hanfu.png\"},{\"value\":\"minzu\",\"name\":\"民族\",\"url\":\"http://cdn.jiwuzao.com/image/category/minzu.png\"},{\"value\":\"mianma\",\"name\":\"棉麻\",\"url\":\"http://cdn.jiwuzao.com/image/category/mianma.png\"}]},{\"value\":\"tongzhuang\",\"name\":\"童装\",\"url\":\"http://cdn.jiwuzao.com/image/category/tongzhuang.png\"}]},{\"value\":\"beauty\",\"name\":\"美业\",\"url\":\"\",\"second\":[{\"value\":\"beauty\",\"name\":\"美业\",\"url\":\"\",\"third\":[{\"value\":\"colorful\",\"name\":\"彩妆\",\"url\":\"http://cdn.jiwuzao.com/image/category/caizhuang.png\"},{\"value\":\"tools\",\"name\":\"美妆工具\",\"url\":\"http://cdn.jiwuzao.com/image/category/meizhuanggongju.png\"},{\"value\":\"mianmo\",\"name\":\"面膜\",\"url\":\"http://cdn.jiwuzao.com/image/category/mianmo.png\"},{\"value\":\"bodycare\",\"name\":\"身体护理\",\"url\":\"http://cdn.jiwuzao.com/image/category/shentihuli.png\"},{\"value\":\"perfume\",\"name\":\"香芬\",\"url\":\"http://cdn.jiwuzao.com/image/category/xiangshui.png\"}]}]},{\"value\":\"special\",\"name\":\"灵物\",\"second\":[{\"value\":\"foxiang\",\"name\":\"佛像\",\"url\":\"http://cdn.jiwuzao.com/image/category/foxiang.png\"},{\"value\":\"tangka\",\"name\":\"唐卡\",\"url\":\"\",\"third\":[{\"value\":\"guotang\",\"name\":\"国唐\",\"url\":\"http://cdn.jiwuzao.com/image/category/guotang.png\"},{\"value\":\"zhitang\",\"name\":\"止唐\",\"url\":\"http://cdn.jiwuzao.com/image/category/zhitang.png\"}]},{\"value\":\"xiangdao\",\"name\":\"香道\",\"url\":\"\",\"third\":[{\"value\":\"xiangqi\",\"name\":\"香器\",\"url\":\"http://cdn.jiwuzao.com/image/category/xiangqi.png\"},{\"value\":\"xianglu\",\"name\":\"香炉\",\"url\":\"http://cdn.jiwuzao.com/image/category/xianglu.png\"},{\"value\":\"xiang\",\"name\":\"香\",\"url\":\"http://cdn.jiwuzao.com/image/category/xiang.png\"}]},{\"value\":\"wenwan\",\"name\":\"文玩\",\"url\":\"\",\"third\":[{\"value\":\"fozhu\",\"name\":\"佛珠\",\"url\":\"http://cdn.jiwuzao.com/image/category/fozhu.png\"},{\"value\":\"shoucuan\",\"name\":\"手串\",\"url\":\"http://cdn.jiwuzao.com/image/category/shouchuan.png\"},{\"value\":\"shanyi\",\"name\":\"扇艺\",\"url\":\"http://cdn.jiwuzao.com/image/category/shanyi.png\"},{\"value\":\"zhudiao\",\"name\":\"竹雕\",\"url\":\"http://cdn.jiwuzao.com/image/category/zhuke.png\"},{\"value\":\"zhuanke\",\"name\":\"篆刻\",\"url\":\"http://cdn.jiwuzao.com/image/category/zhuanke.png\"},{\"value\":\"mudiao\",\"name\":\"木雕\",\"url\":\"http://cdn.jiwuzao.com/image/category/mudiao.png\"},{\"value\":\"shidiao\",\"name\":\"石雕\",\"url\":\"http://cdn.jiwuzao.com/image/category/shidiao.png\"}]}]},{\"value\":\"other\",\"name\":\"其他\",\"second\":[{\"value\":\"other\",\"name\":\"其他\",\"url\":\"http://cdn.jiwuzao.com/image/category/other.png\"}]}]", System.currentTimeMillis()));

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
//        AppVersion appVersion = bean.createUpdateVersion("1.0.9", 109, "http://download.jiwuzao.com/jiwuzao.apk", "添加商品分类和搜索");
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
