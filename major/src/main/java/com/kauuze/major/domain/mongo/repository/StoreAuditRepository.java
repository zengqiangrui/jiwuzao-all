package com.kauuze.major.domain.mongo.repository;

import com.kauuze.major.domain.mongo.entity.userBastic.StoreAudit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-06-10 15:35
 */
@Repository
public interface StoreAuditRepository extends MongoRepository<StoreAudit,String> {
    StoreAudit findByUid(int uid);
}
