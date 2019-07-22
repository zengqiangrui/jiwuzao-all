package com.kauuze.major.domain.mongo.repository;

import com.jiwuzao.common.domain.enumType.ExpressEnum;
import com.jiwuzao.common.domain.mongo.entity.Express;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ExpressRepository extends MongoRepository<Express,String> {
    //根据快递公司代码查找
    Optional<Express> findByCode(String code);

    //根据类型查找
    List<Express> findAllByExpressType(ExpressEnum expressType);

}
