package com.jiwuzao.common.domain.mongo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Document
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Apprise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    //用户为商品点赞的记录
    /**
     * Uid
     */
    private Integer uid;

    /**
     * gid
     */
    private String gid;

    /**
     * 操作时间
     */
    private Long time;
}
