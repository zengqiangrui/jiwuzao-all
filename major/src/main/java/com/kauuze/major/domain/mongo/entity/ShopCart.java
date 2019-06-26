package com.kauuze.major.domain.mongo.entity;

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
 * @time 2019-05-20 17:26
 */
@Document
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ShopCart {
    @Id
    private String id;
    @Indexed
    private Integer uid;
    @Indexed
    private String sid;
    @Indexed
    private String gid;
    private String specId;
    @Indexed
    private String createTime;
}
