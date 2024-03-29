package com.kauuze.manager.api.pojo.userPermission;

import com.jiwuzao.common.domain.enumType.UserStateEnum;
import com.kauuze.manager.include.valid.Idv;
import com.kauuze.manager.include.valid.Mill;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-30 13:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ForbidPojo {
    @Idv
    private Integer uid;
    @NotNull
    private UserStateEnum userState;
    @Mill
    private Long userStateEndTime;
}
