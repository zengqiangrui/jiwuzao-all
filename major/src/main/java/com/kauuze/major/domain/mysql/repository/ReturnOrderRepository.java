package com.kauuze.major.domain.mysql.repository;

import com.jiwuzao.common.domain.enumType.ReturnStatusEnum;
import com.jiwuzao.common.domain.mysql.entity.ReturnOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReturnOrderRepository extends JpaRepository<ReturnOrder,Integer> {
    List<ReturnOrder> findAllByStatus(ReturnStatusEnum status);
    Optional<ReturnOrder> findByGoodsOrderNo(String goodsOrderNo);
}
