package com.jiwuzao.common.domain.mongo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

/**
 * 关注表
 */
@Document
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Focus {
    @Id
    private String id;

    @Indexed
    private Integer uidA;//发起用户

    @Indexed
    private Integer uidB;//接受用户

    /**
     * 是否关注该对象
     */
    @Indexed
    private Boolean status = true;

    private Long createTime;

    private Long updateTime;
}
