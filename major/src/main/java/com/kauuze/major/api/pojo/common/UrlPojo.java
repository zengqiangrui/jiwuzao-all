package com.kauuze.major.api.pojo.common;

import com.kauuze.major.include.valid.Url;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UrlPojo {

    @Url
    private String url;
}
