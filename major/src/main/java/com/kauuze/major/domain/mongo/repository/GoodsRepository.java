package com.kauuze.major.domain.mongo.repository;

import com.jiwuzao.common.domain.enumType.GoodsClassifyEnum;
import com.jiwuzao.common.domain.mongo.entity.Goods;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsRepository extends MongoRepository<Goods,String> {
    Integer countByUid(int uid);

    Goods findByGid(String gid);

    List<Goods> findByUid(int uid);

    Page<Goods> findByUid(int uid,Pageable pageable);


    List<Goods> findByClassify(Pageable pageable , GoodsClassifyEnum classify);

    Page<Goods> findAllBySid(String sid,Pageable pageable);

    Page<Goods> findAllBySidAndPutaway(String storeId, Boolean putAway, Pageable pageable);
}
