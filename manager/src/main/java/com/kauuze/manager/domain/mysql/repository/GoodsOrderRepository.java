package com.kauuze.manager.domain.mysql.repository;


import com.kauuze.manager.domain.enumType.OrderExStatusEnum;
import com.kauuze.manager.domain.mysql.entity.GoodsOrder;
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

    List<GoodsOrder> findBySid(String sid);
    List<GoodsOrder> findByUid(Integer uid);
    List<GoodsOrder> findByOrderExStatus(OrderExStatusEnum status);
}
