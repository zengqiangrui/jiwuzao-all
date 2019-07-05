package com.kauuze.manager.domain.es.repository;

import com.kauuze.manager.domain.es.entity.GoodsEs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public interface GoodsRepositoryEs extends ElasticsearchRepository<GoodsEs, String> {
    List<GoodsEs> findByUid(int uid);
    Page<GoodsEs> findByGid(String gid, Pageable pageable);
}
