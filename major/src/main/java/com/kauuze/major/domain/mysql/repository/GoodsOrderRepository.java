package com.kauuze.major.domain.mysql.repository;

import com.kauuze.major.domain.mysql.entity.GoodsOrder;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-28 11:08
 */
public interface GoodsOrderRepository extends JpaRepository<GoodsOrder,Integer> {
}
