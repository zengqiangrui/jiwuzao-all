package com.kauuze.manager.domain.mongo.repository;


import com.jiwuzao.common.domain.enumType.AuditTypeEnum;
import com.jiwuzao.common.domain.mongo.entity.userBastic.VerifyActor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-25 21:58
 */
@Repository
public interface VerifyActorRepository extends MongoRepository<VerifyActor,String> {
    Page<VerifyActor> findByAuditTypeAndCreateTimeLessThanEqual(AuditTypeEnum auditTypeEnum, Long createTime,
                                                                Pageable pageable);
    Page<VerifyActor> findByCreateTimeLessThanEqual(Long createTime, Pageable pageable);
    Page<VerifyActor> findByAuditType(AuditTypeEnum auditTypeEnum, Pageable pageable);
    VerifyActor findByUid(int uid);
    VerifyActor findByIdcardAndAuditType(String idCard, AuditTypeEnum auditTypeEnum);
    VerifyActor findByUsccAndAuditType(String uscc,AuditTypeEnum auditTypeEnum);
}
