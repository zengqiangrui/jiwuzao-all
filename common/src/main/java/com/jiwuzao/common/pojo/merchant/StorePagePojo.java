package com.jiwuzao.common.pojo.merchant;

import com.jiwuzao.common.pojo.main.PagePojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class StorePagePojo extends PagePojo {
    private String storeId;
}
