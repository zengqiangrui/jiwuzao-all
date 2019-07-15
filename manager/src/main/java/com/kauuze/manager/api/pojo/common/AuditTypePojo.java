package com.kauuze.manager.api.pojo.common;

import com.jiwuzao.common.domain.enumType.AuditTypeEnum;
import com.kauuze.manager.include.valid.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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
