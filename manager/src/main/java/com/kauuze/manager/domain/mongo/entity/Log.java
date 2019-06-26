package com.kauuze.manager.domain.mongo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-24 17:16
 */
@Document
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Log {
    @Id
    private String id;
    @Indexed
    private Long createTime;
    private String log;
    @Indexed
    private String service;
    /**
     * 是否报错
     */
    @Indexed
    private Boolean error;
    private String dateView;

}
