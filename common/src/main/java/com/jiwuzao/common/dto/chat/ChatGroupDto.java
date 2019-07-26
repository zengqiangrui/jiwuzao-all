package com.jiwuzao.common.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ChatGroupDto {

    private Integer uidA;

    private Integer uidB;

    private String groupId;

    private String userNameA;//用户昵称

    private String avatarA;//A的头像

    private String userNameB;

    private String avatarB;



}
