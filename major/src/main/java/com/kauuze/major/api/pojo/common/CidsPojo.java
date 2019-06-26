package com.kauuze.major.api.pojo.common;

import com.kauuze.major.include.valid.ListSizeMax;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kauuze
 * @email 3412879785@qq.com
 * @time 2019-02-24 12:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CidsPojo {
    @ListSizeMax
    private List<CidPojo> cids;

    public List<String> convert(){
        List<String> list = new ArrayList<>();
        for (CidPojo cid : cids) {
            list.add(cid.getCid());
        }
        return list;
    }
}
