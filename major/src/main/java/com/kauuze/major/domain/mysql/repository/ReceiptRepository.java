package com.kauuze.major.domain.mysql.repository;

import com.jiwuzao.common.domain.enumType.ReceiptEnum;
import com.jiwuzao.common.domain.mysql.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt,Integer> {
    /**
     * 根据商品订单查询
     * @param goodsOrderNo
     * @return
     */
    Optional<Receipt> findByGoodsOrderNo(String goodsOrderNo);

    List<Receipt> findByType(ReceiptEnum type);
}
