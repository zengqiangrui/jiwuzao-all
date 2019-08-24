package com.kauuze.major.domain.mongo.repository;

import com.jiwuzao.common.domain.enumType.GoodsClassifyEnum;
import com.jiwuzao.common.domain.mongo.entity.RecommendGoods;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecommendGoodsRepository extends MongoRepository<RecommendGoods,String> {

    List<RecommendGoods> findAllByGoodsClassify(GoodsClassifyEnum goodsClassify);

    Optional<RecommendGoods> findByGoodsClassifyAndStatus(GoodsClassifyEnum goodsClassify, Boolean status);
}
