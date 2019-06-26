package com.kauuze.major.domain.mysql.repository;


import com.kauuze.major.domain.mysql.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-02-24 12:30
 */
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findByPhone(String phone);
    User findByNickName(String nickName);
    User findById(int id);
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select e from User e where e.id = ?1")
    User findByIdForUpdate(int id);
}
