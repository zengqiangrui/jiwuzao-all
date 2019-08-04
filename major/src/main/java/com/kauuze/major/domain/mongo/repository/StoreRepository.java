package com.kauuze.major.domain.mongo.repository;

import com.jiwuzao.common.domain.enumType.StoreStyleEnum;
import com.jiwuzao.common.domain.mongo.entity.userBastic.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-06-08 16:55
 */
@Repository
public interface StoreRepository extends MongoRepository<Store,String> {
    Store findByStoreName(String storeName);

    //Store findByUid(int uid);

    /**
     *
     * @param uid
     * @return
     */
    Optional<Store> findByUid(int uid);

    Page<Store> findByStoreNameLikeAndCreateTimeLessThanEqual(String storeName, Long createTime, Pageable pageable);

    Page<Store> findAllByStoreStyle(StoreStyleEnum style,Pageable pageable);
}
