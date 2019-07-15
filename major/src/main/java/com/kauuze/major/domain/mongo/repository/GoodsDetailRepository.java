package com.kauuze.major.domain.mongo.repository;

import com.kauuze.major.domain.mongo.entity.GoodsDetail;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-06-04 11:05
 */
public interface GoodsDetailRepository extends MongoRepository<GoodsDetail,String> {
    Optional<GoodsDetail> findByGid(String gid);
}
