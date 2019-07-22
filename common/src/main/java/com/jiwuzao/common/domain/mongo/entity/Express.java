package com.jiwuzao.common.domain.mongo.entity;

import com.jiwuzao.common.domain.enumType.ExpressEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Document
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Express {
    @Id
    private String id;

    //快递公司名称
    private String name;

    //快递公司编码
    private String code;

    //快递公司类型，分为常用，国内，国外，转运
    private ExpressEnum expressType;
}
