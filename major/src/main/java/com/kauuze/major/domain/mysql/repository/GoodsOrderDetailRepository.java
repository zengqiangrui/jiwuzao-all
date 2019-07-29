package com.kauuze.major.domain.mysql.repository;

import com.jiwuzao.common.domain.mongo.entity.Goods;
import com.jiwuzao.common.domain.mysql.entity.GoodsOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-06-04 14:19
 */
@Repository
public interface GoodsOrderDetailRepository extends JpaRepository<GoodsOrderDetail,Integer> {
    Optional<GoodsOrderDetail> findByGoodsOrderNo(String goodsOrderNo);
}
