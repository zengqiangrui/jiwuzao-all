package com.kauuze.major.domain.mongo.repository;

import com.jiwuzao.common.domain.mongo.entity.ViewHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViewHistoryRepository extends MongoRepository<ViewHistory,String> {
    List<ViewHistory> findByUidAndDeleteFalse(Integer uid);
    ViewHistory findByUidAndGid(Integer uid, String gid);
}
