package com.jiwuzao.common.pojo.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CancelDeliverPojo {
    private String goodsOrderNo;

    private String pwd;

    private String cancelReason;

}
