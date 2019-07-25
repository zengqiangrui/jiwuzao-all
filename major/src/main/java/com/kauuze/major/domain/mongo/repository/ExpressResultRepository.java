package com.kauuze.major.domain.mongo.repository;

import com.jiwuzao.common.domain.mongo.entity.ExpressResult;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ExpressResultRepository extends MongoRepository<ExpressResult, String> {
    Optional<ExpressResult> findByOrderCode(String orderNo);
    Optional<ExpressResult> findByLogisticCode(String logisticCode);
}
