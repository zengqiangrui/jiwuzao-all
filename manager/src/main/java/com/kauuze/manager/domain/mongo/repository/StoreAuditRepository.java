package com.kauuze.manager.domain.mongo.repository;

import com.jiwuzao.common.domain.mongo.entity.userBastic.StoreAudit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-06-10 16:07
 */
@Repository
public interface StoreAuditRepository extends MongoRepository<StoreAudit,String> {
}
