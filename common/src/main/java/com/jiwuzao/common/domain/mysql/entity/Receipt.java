package com.jiwuzao.common.domain.mysql.entity;

import com.jiwuzao.common.domain.enumType.ReceiptEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.UUID;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
/**
 * 发票只能开增值税普通发票，无法开专票
 * 发票的
 */
public class Receipt {
    @Id
    @GeneratedValue
    private Integer id;//

    private String goodsOrderNo;//商品订单

    private ReceiptEnum type;//个人，企业

    private String name;//名称

    private String goodsDescription;//商品介绍

    private String taxId;//纳税人识别号，如果是企业必填

    private Long createTime;//创建时间

}
