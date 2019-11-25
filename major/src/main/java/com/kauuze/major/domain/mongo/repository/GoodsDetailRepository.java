package com.kauuze.major.domain.mongo.repository;

import com.jiwuzao.common.domain.mongo.entity.GoodsDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-06-04 11:05
 */
@Repository
public interface GoodsDetailRepository extends MongoRepository<GoodsDetail, String> {
    Optional<GoodsDetail> findByGid(String gid);

    /**
     * 根据二级分类查
     *
     * @param goodsSecondClassify
     * @param pageable
     * @return
     */
    Page<GoodsDetail> findAllByGoodsSecondClassify(String goodsSecondClassify, Pageable pageable);


    /**
     * 根据三级分类查
     *
     * @param goodsThirdClassify
     * @param pageable
     * @return
     */
    Page<GoodsDetail> findAllByGoodsThirdClassify(String goodsThirdClassify, Pageable pageable);
}
