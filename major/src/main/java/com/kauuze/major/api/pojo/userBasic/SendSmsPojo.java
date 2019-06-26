package com.kauuze.major.api.pojo.userBasic;

import com.kauuze.major.include.valid.Phone;
import com.kauuze.major.include.valid.StringMax;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-05-21 15:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SendSmsPojo {
    @Phone
    private String phone;
    @StringMax
    private String sha256;
}
