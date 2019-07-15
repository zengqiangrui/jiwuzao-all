package com.kauuze.major.domain.mongo.repository;

import com.kauuze.major.domain.mongo.entity.Goods;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsRepository extends MongoRepository<Goods,String> {
    Integer countByUid(int uid);

    Goods findByGid(String gid);

    List<Goods> findByUid(int uid);


}
