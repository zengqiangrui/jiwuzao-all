package com.jiwuzao.common.domain.mysql.entity;

import com.jiwuzao.common.domain.enumType.ReturnStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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

    private String goodsOrderNo;

    /**
     * 冗余申请用户的 Id和店铺Id以便于查找
     */
    private Integer uid;
    private String storeId;

    private String content;

    private String images;

    @Enumerated(value = EnumType.STRING)
    private ReturnStatusEnum status;

    private String failReason;//失败原因

    private Long createTime;

    private Long updateTime;


}
