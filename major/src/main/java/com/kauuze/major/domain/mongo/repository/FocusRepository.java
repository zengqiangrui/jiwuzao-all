package com.kauuze.major.domain.mongo.repository;

import com.jiwuzao.common.domain.enumType.FocusStatusEnum;
import com.jiwuzao.common.domain.mongo.entity.Focus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FocusRepository extends MongoRepository<Focus,String> {
    Page<Focus> findByUidA(Integer uidA, Pageable pageable);



    Optional<Focus> findByUidAAndUidB(int uidA, int uidB);

    List<Focus> findAllByUidA(int uid);

    List<Focus> findAllByUidB(int uid);

    Integer countByUidAAndStatus(Integer uid, Boolean focusStatus);

    Integer countByUidBAndStatus(Integer uid, Boolean focusStatus);

}
