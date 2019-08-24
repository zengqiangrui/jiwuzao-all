package com.jiwuzao.common.domain.mongo.entity;

import com.jiwuzao.common.domain.enumType.GoodsClassifyEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.math.BigDecimal;

@Document
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class RecommendGoods {

    @Id
    private String id;

    private String goodsId;

    private String name;

    private String cover;

    private BigDecimal price;

    private String reason;

    private GoodsClassifyEnum goodsClassify;

    private Boolean status;

    private Long createTime;
}
