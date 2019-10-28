package com.kauuze.manager.service.dto.storeView;

import com.jiwuzao.common.domain.enumType.StoreStyleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class StoreShowDto {
    private String id;
    private Integer uid;
    private Long createTime;
    private Long modifyTime;

    /**
     * 是否违规：违规下架所有商品，不得再上架。默认未违规
     */
    private Boolean violation = false;
    /**
     * 违规原因：富文本
     */
    private String violationCause;

    /**
     * 店铺名称
     */
    private String storeName;
    /**
     * 店铺图标
     */
    private String storeIcon;

    /**
     * 店铺背景图片
     */
    private String storeBgImg;
    /**
     * 客服电话:短信确认
     */
    private String servicePhone;
    /**
     * 店铺介绍
     */
    private String storeIntro;

    /**
     * 店铺风格，枚举进行选择
     */
    private StoreStyleEnum storeStyle;

    /**
     * 营业执照
     */
    private String businessLicense;

    /**
     * 店铺可提现金额
     */
    private BigDecimal withdrawCash = BigDecimal.ZERO;

    /**
     * 店铺当日已提现次数，每日次数上限可以根据需求设置
     * 同时需要定时任务处理将其归零
     */
    private Integer withdrawNum = 0;
}
