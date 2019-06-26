package com.kauuze.manager.domain.mongo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 主要业务收藏,限制50个
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-14 00:07
 */
@Document
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Collection {
    @Id
    private Integer id;
    @Indexed(unique = true)
    private Integer cid;
    @Indexed
    private Long createTime;

    /**
     * 内容封面
     */
    private String contentCover;
    /**
     * 标题
     */
    private String title;
    /**
     * 发布者昵称
     */
    private String nickName;
    /**
     * 是否认证用户
     */
    private Boolean verifyActor;
    /**
     * 发布时间
     */
    private Long upTime;
}
