package com.jiwuzao.common.pojo.common;


import com.jiwuzao.common.domain.enumType.AuditTypeEnum;
import com.jiwuzao.common.include.valid.Page;
import com.jiwuzao.common.include.valid.StringMax;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-22 15:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AuditTypePojo {
    private AuditTypeEnum auditType;
    @Page
    private PagePojo page;
}
