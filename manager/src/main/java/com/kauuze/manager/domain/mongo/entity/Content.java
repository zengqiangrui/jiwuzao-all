package com.kauuze.manager.domain.mongo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 主营业务内容
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-13 22:52
 */
@Document
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Content {
    @Id
    private String id;
    @Indexed(unique = true)
    private Integer uid;

    /**
     * 访问时如失去关联删除
     */
    @Indexed(unique = true)
    private Integer sid;
}
