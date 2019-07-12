package com.kauuze.major.service;

import com.kauuze.major.domain.mongo.repository.*;
import com.kauuze.major.domain.mysql.repository.PayOrderRepository;
import com.kauuze.major.domain.mysql.repository.UserRepository;
import com.kauuze.major.domain.mysql.repository.WithdrawOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
