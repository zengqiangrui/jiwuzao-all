package com.jiwuzao.common.pojo.userBasic;

import com.jiwuzao.common.domain.enumType.SexEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AlterGenderPojo {
    SexEnum gender;
}
