package com.kauuze.major.domain.mongo.repository;

import com.jiwuzao.common.domain.mongo.entity.ShopCart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopCartRepository extends MongoRepository<ShopCart,String> {
    List<ShopCart> findByUid(Integer uid);

    ShopCart findByUidAndSpecId(Integer uid, String sid);
}
