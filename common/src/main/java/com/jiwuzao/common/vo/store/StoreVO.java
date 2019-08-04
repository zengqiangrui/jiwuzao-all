package com.jiwuzao.common.vo.store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class StoreVO {
    private String storeId;

    private String storeName;

    private String storeIcon;//头像

    private String storeBgImg;//背景图

    private String personSign;//用户，个性签名

    private String storeIntro;//富文本介绍

}
