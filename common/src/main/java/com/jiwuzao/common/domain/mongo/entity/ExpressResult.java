package com.jiwuzao.common.domain.mongo.entity;

import com.jiwuzao.common.dto.express.ExpressTraceDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ExpressResult {
    @Id
    private String id;

    private String pushTime;
    /**
     * 物流运单号
     */
    private String logisticCode;

    private String eBusinessID;

    /**
     * 快递公司编码，必须
     * @see com.jiwuzao.common.domain.mongo.entity.Express
     */
    private String shipperCode;

    /**
     * 订单编号，非必须
     */
    private String orderCode;

    /**
     * 成功与否
     */
    private Boolean success;

    private String reason;

    /**
     * 物流状态：2-在途中,3-签收,4-问题件
     */
    private String state;

    private List<ExpressTraceDto> traces;
}
