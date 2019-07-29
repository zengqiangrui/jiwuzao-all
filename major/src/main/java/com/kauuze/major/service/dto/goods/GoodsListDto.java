package com.kauuze.major.service.dto.goods;

import com.jiwuzao.common.domain.mongo.entity.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Created by Enzo Cotter on 2019/7/26.
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class GoodsListDto {
    private Goods goods;
}
