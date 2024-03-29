package com.jiwuzao.common.pojo.common;


import com.jiwuzao.common.include.valid.Idv;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Uid传参
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UidPojo {
    @Idv
    private Integer uid;
}
