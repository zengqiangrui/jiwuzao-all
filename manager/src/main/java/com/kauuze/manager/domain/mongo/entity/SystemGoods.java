package com.kauuze.manager.domain.mongo.entity;


import com.kauuze.manager.domain.enumType.SystemGoodsNameEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * 系统即时商品
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-20 17:15
 */
@Document
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SystemGoods {
    @Id
    private String id;
    /**
     * 名称
     */
    @Indexed
    private SystemGoodsNameEnum name;
    /**
     * 中文名称
     */
    private String title;
    /**
     * 封面
     */
    private String cover;
    /**
     * 价格
     */
    private BigDecimal price;
}
