package com.kauuze.major.domain.mongo.repository;

import com.jiwuzao.common.domain.mongo.entity.ExpressResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ExpressResultRepository extends MongoRepository<ExpressResult, String> {
    Optional<ExpressResult> findByOrderCode(String orderNo);
    Optional<ExpressResult> findByLogisticCode(String logisticCode);
}
