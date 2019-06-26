package com.kauuze.major.domain.mongo.repository;


import com.kauuze.major.domain.enumType.AuditTypeEnum;
import com.kauuze.major.domain.mongo.entity.userBastic.VerifyActor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-25 21:58
 */
@Repository
public interface VerifyActorRepository extends MongoRepository<VerifyActor,String> {
    VerifyActor findByUid(int uid);
    VerifyActor findByIdcardAndAuditType(String idCard, AuditTypeEnum auditTypeEnum);
    VerifyActor findByUsccAndAuditType(String uscc,AuditTypeEnum auditTypeEnum);
}
