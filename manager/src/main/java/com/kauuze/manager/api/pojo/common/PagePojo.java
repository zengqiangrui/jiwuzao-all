package com.kauuze.manager.api.pojo.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-27 22:58
 */
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Data
public class PagePojo {
    private Integer num;
    private Integer size;
    private Long time;
}
