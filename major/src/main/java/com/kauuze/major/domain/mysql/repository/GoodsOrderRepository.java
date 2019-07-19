package com.kauuze.major.domain.mysql.repository;

import com.jiwuzao.common.domain.mysql.entity.GoodsOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-28 11:08
 */
@Repository
public interface GoodsOrderRepository extends JpaRepository<GoodsOrder,Integer> {
    List<GoodsOrder> findByUid(int uid);
    List<GoodsOrder> findByPid(String pid);
    GoodsOrder findByGoodsOrderNo(String goodsOrderNo);
}
