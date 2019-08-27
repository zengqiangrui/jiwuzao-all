package com.jiwuzao.common.vo.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 最新app版本vo
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class AppUpdateVO {
    private String appVersionName;

    private Integer appVersionCode;

    private String updateMsg;
    private Long lastUpdateTime;
}
