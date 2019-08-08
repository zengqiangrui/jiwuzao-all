package com.jiwuzao.common.domain.mongo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Document
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String comid;
    /**
     * 商品订单id
     */
    private String goodsOrderNo;

    /**
     * 商品id
     */
    @Indexed
    private String gid;

    /**
     * uid
     */
    @Indexed
    private Integer uid;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论时间
     */
    private Long time;
    /**
     * 是否删除
     */
    private boolean delete;
}
