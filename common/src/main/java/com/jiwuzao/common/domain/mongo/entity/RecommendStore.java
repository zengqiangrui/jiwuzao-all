package com.jiwuzao.common.domain.mongo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Max;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document
@Accessors(chain = true)
public class RecommendStore {

    @Id
    private String id;

    private String storeId;//店铺id

    private String storeName;//店铺名

    private List<String> images;//显示图片数组

    private Long createTime;

}
