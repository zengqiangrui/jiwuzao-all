package com.kauuze.major.domain.mongo.entity;

import com.kauuze.major.domain.enumType.ComplainTypeEnum;
import com.kauuze.major.domain.enumType.WorkOrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 工单
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-13 23:17
 */
@Document
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class WorkOrder {
    @Id
    private String id;
    @Indexed(unique = true)
    private Integer uid;
    @Indexed
    private Long createTime;
    @Indexed
    private Integer cmsId;

    /**
     * 投诉类型
     */
    private ComplainTypeEnum complainType;
    /**
     * 本地采集的信息[]
     */
    private String localInfo;
    /**
     * 问题描述
     */
    private String describe;
    /**
     * 问题截图
     */
    private String screenshot;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 联系姓名
     */
    private String trueName;

    /**
     * 工单状态
     */
    private WorkOrderStatusEnum woStatus;
    /**
     * 处理时间
     */
    private Long handTime;
    /**
     * 已处理时间
     */
    private Long handedTime;
    /**
     * 结单时间
     */
    private Long finishTime;
}
