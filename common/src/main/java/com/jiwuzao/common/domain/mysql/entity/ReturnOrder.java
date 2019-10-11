package com.jiwuzao.common.domain.mysql.entity;

import com.jiwuzao.common.domain.enumType.ReturnStatusEnum;
import com.jiwuzao.common.domain.mongo.entity.Express;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.persistence.*;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class ReturnOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String goodsOrderNo;//商品订单

    private String goodsReturnNo;//商品退款单号

    /**
     * 冗余申请用户的 Id和商品id店铺Id以便于查找
     */
    private Integer uid;
    private String storeId;

    private String content;

    private String images;

    @Enumerated(value = EnumType.STRING)
    private ReturnStatusEnum status;

    /**
     * 快递公司编码
     */
    private String expCode;

    /**
     * 快递订单号
     */
    private String expNo;

    private String failReason;//失败原因

    private Long createTime;

    private Long updateTime;


}
