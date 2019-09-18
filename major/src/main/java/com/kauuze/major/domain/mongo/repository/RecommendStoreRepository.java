package com.kauuze.major.domain.mongo.repository;

import com.jiwuzao.common.domain.mongo.entity.RecommendStore;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RecommendStoreRepository extends MongoRepository<RecommendStore,String> {
}
