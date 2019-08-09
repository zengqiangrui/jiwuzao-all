package com.jiwuzao.common.vo.chat;

import com.jiwuzao.common.domain.enumType.OnlineStatusEnum;
import com.jiwuzao.common.domain.enumType.SexEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ChatGroupItemVO {
    private Integer uid;
    
    private String groupId;
    
    private String avatar;
    
    private String nickName;

    private OnlineStatusEnum onlineStatus;

    private SexEnum sex;
    
    private Integer undoNum;
    
    private String message;

}
