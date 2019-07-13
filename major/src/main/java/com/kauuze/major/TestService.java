package com.kauuze.major;

import com.kauuze.major.domain.mongo.repository.GoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-31 11:38
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class TestService {
//    @Autowired
//    private GoodsRepository goodsRepository;
    public void test(){
    }
}
