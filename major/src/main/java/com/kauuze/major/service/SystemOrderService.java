package com.kauuze.major.service;

import com.kauuze.major.ConfigUtil;
import com.kauuze.major.domain.common.EsUtil;
import com.kauuze.major.domain.common.OrderUtil;
import com.kauuze.major.domain.enumType.AuditTypeEnum;
import com.kauuze.major.domain.enumType.PayCallBackUrl;
import com.kauuze.major.domain.enumType.SystemGoodsNameEnum;
import com.kauuze.major.domain.enumType.WithdrawStatusEnum;
import com.kauuze.major.domain.es.entity.Goods;
import com.kauuze.major.domain.es.repository.GoodsRepository;
import com.kauuze.major.domain.mongo.entity.SystemGoods;
import com.kauuze.major.domain.mongo.entity.userBastic.Store;
import com.kauuze.major.domain.mongo.entity.userBastic.VerifyActor;
import com.kauuze.major.domain.mongo.repository.StoreRepository;
import com.kauuze.major.domain.mongo.repository.SystemGoodsRepository;
import com.kauuze.major.domain.mongo.repository.UserTokenRepository;
import com.kauuze.major.domain.mongo.repository.VerifyActorRepository;
import com.kauuze.major.domain.mysql.entity.PayOrder;
import com.kauuze.major.domain.mysql.entity.User;
import com.kauuze.major.domain.mysql.entity.WithdrawOrder;
import com.kauuze.major.domain.mysql.repository.PayOrderRepository;
import com.kauuze.major.domain.mysql.repository.UserRepository;
import com.kauuze.major.domain.mysql.repository.WithdrawOrderRepository;
import com.kauuze.major.include.yun.WxPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-25 16:37
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class SystemOrderService {
    @Autowired
    private PayOrderRepository payOrderRepository;
    @Autowired
    private SystemGoodsRepository systemGoodsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserTokenRepository userTokenRepository;
    @Autowired
    private VerifyActorRepository verifyActorRepository;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private WithdrawOrderRepository withdrawOrderRepository;
    @Autowired
    private StoreRepository storeRepository;

}
