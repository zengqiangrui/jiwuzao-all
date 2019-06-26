package com.kauuze.major.domain.es.repository;

import com.kauuze.major.domain.es.entity.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-02-27 13:27
 */
@Repository
public interface GoodsRepository extends ElasticsearchRepository<Goods, String> {
    List<Goods> findByUid(int uid);
    Goods findByGid(String sid);
    int countByUid(int uid);
}
