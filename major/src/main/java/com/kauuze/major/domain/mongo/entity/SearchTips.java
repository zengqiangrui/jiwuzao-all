package com.kauuze.major.domain.mongo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 搜索提示词：人工审核content时添加
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-21 21:46
 */
@Document
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SearchTips {
    @Id
    private String id;
    /**
     * 搜索提示词:最长20字
     */
    @Indexed(unique = true)
    private String tips;
    /**
     * 搜索提示词点击量
     */
    @Indexed
    private Integer hits;
    /**
     * 每周点击量：大家都在搜，周一置零
     */
    @Indexed
    private Integer weekHits;
}
