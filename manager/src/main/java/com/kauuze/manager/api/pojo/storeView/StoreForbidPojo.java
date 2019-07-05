package com.kauuze.manager.api.pojo.storeView;

import com.kauuze.manager.domain.enumType.UserStateEnum;
import com.kauuze.manager.include.valid.Idv;
import com.kauuze.manager.include.valid.Mill;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class StoreForbidPojo {
    @Idv
    private Integer uid;

    private String violationCause;
}
