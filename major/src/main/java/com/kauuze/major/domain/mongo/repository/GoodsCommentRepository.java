package com.kauuze.major.domain.mongo.repository;

import com.jiwuzao.common.domain.mongo.entity.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsCommentRepository extends MongoRepository<Comment,String> {
    List<Comment> findByGid(String gid);
}
