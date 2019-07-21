package com.kauuze.manager.domain.mysql.repository;

import com.jiwuzao.common.domain.mysql.entity.GoodsOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsOrderDetailRepository extends JpaRepository<GoodsOrderDetail,Integer> {
}
