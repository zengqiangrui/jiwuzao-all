package com.kauuze.major.domain.mongo.repository;


import com.jiwuzao.common.domain.mongo.entity.Apprise;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AppriseRepository extends MongoRepository<Apprise,String> {
    Long countByGid(String gid);
    Apprise findByGidAndUid(String gid, Integer uid);
    List<Apprise> findByGid(String gid);
    List<Apprise> findByUid(Integer uid);
}
