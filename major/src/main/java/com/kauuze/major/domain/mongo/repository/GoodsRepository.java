package com.kauuze.major.domain.mongo.repository;

import com.jiwuzao.common.domain.enumType.AuditTypeEnum;
import com.jiwuzao.common.domain.enumType.GoodsClassifyEnum;
import com.jiwuzao.common.domain.mongo.entity.Goods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsRepository extends MongoRepository<Goods, String> {
    Integer countByUid(int uid);

    Goods findByGid(String gid);

    List<Goods> findByUid(int uid);

    Page<Goods> findByUid(int uid, Pageable pageable);

    Page<Goods> findAllByUidAndAuditType(int uid, AuditTypeEnum auditType, Pageable pageAble);

    Page<Goods> findAllByUidAndPutaway(int uid, Boolean putAway, Pageable pageable);

    List<Goods> findByClassifyAndPutaway(Pageable pageable, GoodsClassifyEnum classify, Boolean putaway);

    Page<Goods> findAllBySid(String sid, Pageable pageable);

    Page<Goods> findAllBySidAndPutaway(String storeId, Boolean putAway, Pageable pageable);

    List<Goods> findAllBySidAndPutaway(String sid, Boolean putaway);

    List<Goods> findByTitleLike(String tips);

    /**
     * 根据二级分类查找是否上架商品
     * @param secondClassify
     * @param putAway
     * @param pageable
     * @return
     */
    Page<Goods> findAllByGoodsSecondClassifyAndPutaway(String secondClassify, Boolean putAway, Pageable pageable);

    /**
     * 根据三级分类查找是否上架商品
     * @param thirdClassify
     * @param putAway
     * @param pageable
     * @return
     */
    Page<Goods> findAllByGoodsThirdClassifyAndPutaway(String thirdClassify, Boolean putAway, Pageable pageable);

}
