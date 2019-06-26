package com.kauuze.order.domain.mongo.repository;

import com.kauuze.order.domain.mongo.entity.Log;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-06-09 20:56
 */
public interface UserInfoRepository extends MongoRepository<Log,String> {
}
