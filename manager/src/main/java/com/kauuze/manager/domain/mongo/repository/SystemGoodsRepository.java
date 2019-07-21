package com.kauuze.manager.domain.mongo.repository;


import com.jiwuzao.common.domain.enumType.SystemGoodsNameEnum;
import com.jiwuzao.common.domain.mongo.entity.SystemGoods;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-22 10:54
 */
@Repository
public interface SystemGoodsRepository extends MongoRepository<SystemGoods,String> {
    SystemGoods findByName(SystemGoodsNameEnum systemGoodsNameEnum);
}
