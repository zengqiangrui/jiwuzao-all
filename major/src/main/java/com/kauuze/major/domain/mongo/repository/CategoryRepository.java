package com.kauuze.major.domain.mongo.repository;

import com.kauuze.major.domain.mongo.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends MongoRepository<Category,Integer> {
}
