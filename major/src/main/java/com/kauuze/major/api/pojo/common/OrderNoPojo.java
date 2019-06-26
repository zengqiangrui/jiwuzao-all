package com.kauuze.major.api.pojo.common;

import com.kauuze.major.include.valid.OrderNo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-23 09:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class OrderNoPojo {
    @OrderNo
    private String orderNo;
}
