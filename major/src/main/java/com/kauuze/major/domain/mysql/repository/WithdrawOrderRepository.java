package com.kauuze.major.domain.mysql.repository;

import com.jiwuzao.common.domain.enumType.WithdrawStatusEnum;
import com.jiwuzao.common.domain.mysql.entity.WithdrawOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-28 11:06
 */
@Repository
public interface WithdrawOrderRepository extends JpaRepository<WithdrawOrder,Integer> {
    List<WithdrawOrder> findByUidAndWithdrawStatusNot(int uid, WithdrawStatusEnum WithdrawStatusEnum);
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select e from WithdrawOrder e where e.id = ?1")
    WithdrawOrder findByIdForUpdate(int id);

    List<WithdrawOrder> findAllByWithdrawStatus(WithdrawStatusEnum withdrawStatus);

    Optional<WithdrawOrder> findByStoreIdAndWithdrawStatus(String storeId,WithdrawStatusEnum status);

    Optional<WithdrawOrder> findByWithdrawOrderNo(String withdrawOrderNo);

    List<WithdrawOrder> findAllByStoreIdOrderByCreateTimeDesc(String storeId);

    List<WithdrawOrder> findAllByStoreId(String storeId);
}
