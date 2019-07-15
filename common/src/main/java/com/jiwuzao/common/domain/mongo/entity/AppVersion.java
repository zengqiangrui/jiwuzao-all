package com.jiwuzao.common.domain.mongo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 版本
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-07 14:46
 */
@Document
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class AppVersion {
    @Id
    private String id;
    /**
     * 发布时间
     */
    @Indexed(unique = true)
    private Long createTime;
    /**
     * 版本号
     */
    @Indexed(unique = true)
    private String version;
    /**
     * 更新内容
     */
    private String updateContent;
    /**
     * 下载地址
     */
    private String downloadUrl;
    /**
     * 是否停止使用
     */
    @Indexed
    private Boolean stop;
    /**
     * 停止使用时间
     */
    private Long stopTime;
}
