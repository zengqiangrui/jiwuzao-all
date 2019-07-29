package com.kauuze.major.domain.mongo;

import com.jiwuzao.common.domain.mongo.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ChatMessageRepository extends MongoRepository<ChatMessage,String> {
    Page<ChatMessage> findAllByGroupId(String groupId, Pageable pageable);
}
