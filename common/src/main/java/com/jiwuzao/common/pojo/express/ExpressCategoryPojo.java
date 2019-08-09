package com.jiwuzao.common.pojo.express;

import com.jiwuzao.common.domain.enumType.ExpressEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ExpressCategoryPojo {
    private ExpressEnum expressStatus = ExpressEnum.COMMON;//快递公司类型，默认是常用快递公司
}
