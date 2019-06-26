package com.kauuze.manager.domain.mongo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * 商品规格
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-20 17:55
 */
@Document
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class GoodsSpec {
    @Id
    private String id;
    @Indexed
    private String gid;
    /**
     * 规格分类(逗号分隔)
     */
    @Indexed
    private String specClass;
    /**
     * 规格价格
     */
    private BigDecimal specPrice;
    /**
     * 规格库存
     */
    private Integer specInventory;
}
