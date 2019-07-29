package com.jiwuzao.common.pojo.order;

import com.jiwuzao.common.pojo.shopcart.AddItemPojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class GenOrderPojo {
    private List<AddItemPojo> itemList;


}
