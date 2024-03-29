package com.kauuze.order.domain.es.repository;


import com.kauuze.order.domain.es.entity.Goods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-02-27 13:27
 */
@Repository
public interface GoodsRepository extends ElasticsearchRepository<Goods, String> {
    Page<Goods> findByGid(int gid, Pageable pageable);
}
