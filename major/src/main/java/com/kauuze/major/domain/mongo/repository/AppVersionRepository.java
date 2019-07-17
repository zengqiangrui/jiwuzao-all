package com.kauuze.major.domain.mongo.repository;

import com.jiwuzao.common.domain.mongo.entity.AppVersion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-07 15:15
 */
@Repository
public interface AppVersionRepository extends MongoRepository<AppVersion,String> {
    AppVersion findByVersion(String version);
}
