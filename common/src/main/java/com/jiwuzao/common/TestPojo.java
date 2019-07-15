package com.jiwuzao.common;

import com.kauuze.major.domain.enumType.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-06-04 13:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TestPojo {
    @NotNull
    private OrderStatusEnum test;
}
