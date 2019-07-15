package com.kauuze.major.api.pojo.goods;

import com.kauuze.major.include.valid.Idv;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CategoryPojo {
    private Integer categoryId;
}
