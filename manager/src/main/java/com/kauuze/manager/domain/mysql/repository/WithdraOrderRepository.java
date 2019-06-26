package com.kauuze.manager.domain.mysql.repository;


import com.kauuze.manager.domain.mysql.entity.WithdrawOrder;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-28 11:06
 */
public interface WithdraOrderRepository extends JpaRepository<WithdrawOrder,Integer> {
}
