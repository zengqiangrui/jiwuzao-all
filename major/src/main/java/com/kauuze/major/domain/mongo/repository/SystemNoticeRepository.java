package com.kauuze.major.domain.mongo.repository;

import com.jiwuzao.common.domain.mongo.entity.SystemNotice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-07 15:07
 */
@Repository
public interface SystemNoticeRepository extends MongoRepository<SystemNotice,String>  {
}
