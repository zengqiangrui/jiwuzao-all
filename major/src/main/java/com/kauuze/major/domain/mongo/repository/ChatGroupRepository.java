package com.kauuze.major.domain.mongo.repository;

import com.jiwuzao.common.domain.mongo.entity.ChatGroup;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ChatGroupRepository extends MongoRepository<ChatGroup, String> {
    Optional<ChatGroup> getByUidAAndUidB(Integer uidA, Integer uidB);
    List<ChatGroup> findByUidAOrUidB(Integer uidA, Integer uidB);

}
