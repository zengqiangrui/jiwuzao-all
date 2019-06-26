package com.kauuze.manager.domain.mysql.repository;

import com.kauuze.manager.domain.mysql.entity.PayOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-23 11:37
 */
@Repository
public interface PayOrderRepository extends JpaRepository<PayOrder,Integer> {

    PayOrder findByPay(String orderNo);
    List<PayOrder> findByUid(int uid);

    PayOrder findById(int id);
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select e from PayOrder e where e.id = ?1")
    PayOrder findByIdForUpdate(int id);
}
