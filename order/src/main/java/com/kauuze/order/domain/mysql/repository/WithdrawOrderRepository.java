package com.kauuze.order.domain.mysql.repository;


import com.kauuze.order.domain.mysql.entity.WithdrawOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-28 11:06
 */
public interface WithdrawOrderRepository extends JpaRepository<WithdrawOrder,Integer> {
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select e from WithdrawOrder e where e.id = ?1")
    WithdrawOrder findByIdForUpdate(int id);
}
