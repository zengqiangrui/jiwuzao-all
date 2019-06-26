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
 * @time 2019-04-23 09:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class GidsPojo {
    @ListSizeMax
    private List<GidPojo> gids;

    public List<String> convert(){
        List<String> list = new ArrayList<>();
        for (GidPojo gid : gids) {
            list.add(gid.getGid());
        }
        return list;
    }
}
