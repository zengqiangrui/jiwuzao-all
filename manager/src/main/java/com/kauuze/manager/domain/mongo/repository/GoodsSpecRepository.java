package com.kauuze.manager.domain.mongo.repository;

import com.jiwuzao.common.domain.mongo.entity.Goods;
import com.jiwuzao.common.domain.mongo.entity.GoodsSpec;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-06-04 11:05
 */
public interface GoodsSpecRepository extends MongoRepository<GoodsSpec,String>  {
    List<GoodsSpec> findByGid(String gid);
    void deleteByGid(String gid);

}
