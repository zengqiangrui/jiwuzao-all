package com.kauuze.major.domain.mysql.repository;

import com.jiwuzao.common.domain.mysql.entity.Sms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-25 17:02
 */
@Repository
public interface SmsRepository extends JpaRepository<Sms,Integer> {
    Sms findByPhone(String phone);
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select e from Sms e where e.id = ?1")
    Sms findByIdForUpdate(int id);
}
