package com.kauuze.major.domain.mongo.repository;


import com.jiwuzao.common.domain.mongo.entity.Apprise;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppriseRepository extends MongoRepository<Apprise,String> {
    Long countByGid(String gid);
    Apprise findByGidAndUid(String gid, Integer uid);
}
