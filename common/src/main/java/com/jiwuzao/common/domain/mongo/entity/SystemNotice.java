package com.jiwuzao.common.domain.mongo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 系统公告,仅一条
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-07 14:42
 */
@Document
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SystemNotice {
    @Id
    private String id;
    private String createTime;
    /**
     * 公告内容:html
     */
    private String content;
    /**
     * 是否系统维护
     */
    private Boolean maintain;
}
