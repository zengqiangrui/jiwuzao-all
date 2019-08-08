package com.kauuze.major.domain.mongo.repository;

import com.jiwuzao.common.domain.mongo.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    Page<ChatMessage> findAllByGroupId(String groupId, Pageable pageable);

    List<ChatMessage> findAllByGroupId(String groupId);

    /**
     * 获取用户未处理信息数量
     *
     * @param groupId
     * @param status
     * @param uid
     * @return
     */
    Integer countByGroupIdAndStatusAndUidIsNot(String groupId, Integer status, Integer uid);

    List<ChatMessage> findAllByGroupIdAndStatusAndUidIsNot(String groupId, Integer status, Integer uid);
}
