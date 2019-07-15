package com.kauuze.manager.service.dto.Goods;

import com.kauuze.manager.domain.mongo.entity.Goods;
import com.kauuze.manager.domain.mongo.entity.GoodsDetail;
import com.kauuze.manager.domain.mongo.entity.GoodsSpec;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class GoodsShowDto {
    Goods goods;
    GoodsDetail goodsDetail;
    GoodsSpec goodsSpec;
}
