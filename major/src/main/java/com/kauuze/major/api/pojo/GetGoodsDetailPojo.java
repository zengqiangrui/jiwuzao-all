package com.kauuze.major.api.pojo;

import com.jiwuzao.common.include.valid.Idv;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class GetGoodsDetailPojo {
    @Idv
    private String gid;
    @Idv(require = false)
    private Integer uid;
}