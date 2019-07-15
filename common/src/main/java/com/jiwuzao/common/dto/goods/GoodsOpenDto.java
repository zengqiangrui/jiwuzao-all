package com.jiwuzao.common.dto.goods;

import com.jiwuzao.common.domain.enumType.AuditTypeEnum;
import com.jiwuzao.common.domain.enumType.GoodsClassifyEnum;
import com.jiwuzao.common.domain.mongo.entity.GoodsSpec;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-06-09 15:44
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class GoodsOpenDto {
    private String gid;
    private Integer uid;
    private String sid;
    /**
     * 营业执照
     */
    private String businessLicense;
    /**
     * 客服电话
     */
    private String servicePhone;
    /**
     * 标题
     */
    private String title;
    /**
     * 封面
     */
    private String cover;
    /**
     * 分类
     */
    private GoodsClassifyEnum classify;
    /**
     * 销量
     */
    private Integer salesVolume;
    /**
     * 默认价格
     */
    private BigDecimal defaultPrice;
    /**
     * 邮费
     */
    private BigDecimal postage;
    /**
     * 短视频或图片轮播最多5，分析连接
     */
    private String slideshow;
    /**
     *详情标签,最多5个,每个5字
     */
    private String detailLabel;
    /**
     * 详情图:可以9张
     */
    private String detailPhotos;
    /**
     * 商品类型
     */
    private String goodsType;
    /**
     * 商品类型分类
     */
    private String goodsTypeClass;
    /**
     * 获取商品规格
     */
    private List<GoodsSpec> goodsSpecs;
    /**
     * 是否上架
     */
    private Boolean putaway;
    /**
     * 商品审核状态
     */
    private AuditTypeEnum auditType;
    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;
}
