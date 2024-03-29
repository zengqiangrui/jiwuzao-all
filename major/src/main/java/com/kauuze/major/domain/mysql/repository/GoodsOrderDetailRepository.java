package com.kauuze.major.domain.mysql.repository;

import com.jiwuzao.common.domain.mysql.entity.GoodsOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-06-04 14:19
 */
@Repository
public interface GoodsOrderDetailRepository extends JpaRepository<GoodsOrderDetail,Integer> {
    Optional<GoodsOrderDetail> findByGoodsOrderNo(String goodsOrderNo);

    Optional<GoodsOrderDetail> findByRefundOrderNo(String refundNo);

    /**
     * 根据物流单号查询
     * @param expressNo
     * @return
     */
    Optional<GoodsOrderDetail> findByExpressNo(String expressNo);
}
