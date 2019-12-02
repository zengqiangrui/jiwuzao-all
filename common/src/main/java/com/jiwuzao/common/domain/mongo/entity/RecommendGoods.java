package com.jiwuzao.common.domain.mongo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.List;

@Document
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class RecommendGoods {

    @Id
    private Integer id;

    /**
     * 商品ids
     */
    private List<String> gids;

    private String reason;

    private Boolean status;

    private Long createTime;
}
