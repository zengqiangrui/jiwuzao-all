package com.jiwuzao.common.domain.mongo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ViewHistory {
    //用户商品浏览记录

    @Id
    private String id;

    /**
     * uid
     */
    private Integer uid;

    /**
     * gid
     */
    private String gid;

    /**
     * 浏览时间
     */
    private Long time;

    /**
     * 是否删除
     */
    private boolean delete;
}
