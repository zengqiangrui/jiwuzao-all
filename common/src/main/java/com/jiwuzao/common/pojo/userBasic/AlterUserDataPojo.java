package com.jiwuzao.common.pojo.userBasic;

import com.jiwuzao.common.include.valid.Mill;
import com.jiwuzao.common.include.valid.QQ;
import com.jiwuzao.common.include.valid.StringMax;
import com.jiwuzao.common.include.valid.WxId;
import com.kauuze.major.domain.enumType.SexEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 *用户资料
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-04-24 17:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AlterUserDataPojo {
    /**
     * 性别
     */
    @NotNull
    private SexEnum sex;
    /**
     * 生日
     */
    @Mill(require = false)
    private Long birthday;
    /**
     * 省份
     */
    @StringMax(max = 20,require = false)
    private String province;
    /**
     * 城市
     */
    @StringMax(max = 20,require = false)
    private String city;
    /**
     * 公开微信号
     */
    @WxId(require = false)
    private String openWxId;
    /**
     * 公开qq号
     */
    @QQ(require = false)
    private String openQQ;
}
