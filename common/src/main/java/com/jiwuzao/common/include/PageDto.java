package com.jiwuzao.common.include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-26 13:28
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class PageDto<T> {
    private Long total;
    private List<T> content;
}
