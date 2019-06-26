package com.kauuze.manager.domain.mysql.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-20 14:10
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class SpecOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 规格分类(多个逗号分隔)
     */
    private String specClass;
    /**
     * 规格单价
     */
    private String specPrice;
    /**
     * 购买数量
     */
    private Integer buyCount;
}
