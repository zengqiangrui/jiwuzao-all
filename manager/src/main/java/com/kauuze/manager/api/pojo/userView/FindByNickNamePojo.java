package com.kauuze.manager.api.pojo.userView;

import com.kauuze.manager.include.valid.NickName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class FindByNickNamePojo {
    @NickName
    private String nickName;
    //分页查询参数
    private Integer num;
    private Integer size;
}
