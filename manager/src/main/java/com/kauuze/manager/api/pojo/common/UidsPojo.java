package com.kauuze.manager.api.pojo.common;


import com.kauuze.manager.include.valid.ListSizeMax;
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
public class UidsPojo {
    @ListSizeMax
    private List<UidPojo> uids;
    public List<Integer> convert(){
        List<Integer> list = new ArrayList<>();
        for (UidPojo uid : uids) {
            list.add(uid.getUid());
        }
        return list;
    }
}
