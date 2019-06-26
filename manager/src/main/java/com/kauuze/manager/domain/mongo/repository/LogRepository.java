package com.kauuze.manager.domain.mongo.repository;

import com.kauuze.manager.domain.mongo.entity.Log;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-24 17:18
 */
@Repository
public interface LogRepository extends MongoRepository<Log,String> {

}
