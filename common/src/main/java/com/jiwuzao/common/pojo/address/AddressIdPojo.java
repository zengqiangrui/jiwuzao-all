package com.jiwuzao.common.pojo.address;

import com.jiwuzao.common.include.valid.Idv;
import com.jiwuzao.common.include.valid.StringMax;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AddressIdPojo {
    @StringMax(max = 32)
    private String addressId;
}
