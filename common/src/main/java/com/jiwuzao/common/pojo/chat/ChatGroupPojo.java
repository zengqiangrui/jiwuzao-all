package com.jiwuzao.common.pojo.chat;


import com.jiwuzao.common.include.valid.StringMax;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ChatGroupPojo {
    @StringMax
    private String groupId;

    private Integer pageNum = 0;

    private Integer pageSize = 1000;

    private String createBy = "createTime";

    private Boolean isAsc = true;//排序方式，是否升序
}
