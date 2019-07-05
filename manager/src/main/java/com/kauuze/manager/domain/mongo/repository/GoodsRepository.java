package com.kauuze.manager.domain.mongo.repository;

import com.kauuze.manager.domain.mongo.entity.Goods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-02-27 13:27
 */
@Repository
public interface GoodsRepository extends MongoRepository<Goods, String> {
    List<Goods> findByUid(int uid);
    Goods findByGid(String gid);
    Page<Goods> findBySid(String sid, Pageable pageable);
}
