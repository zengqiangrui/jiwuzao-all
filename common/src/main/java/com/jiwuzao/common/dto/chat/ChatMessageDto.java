package com.jiwuzao.common.dto.chat;

import com.jiwuzao.common.domain.enumType.MessageTypeEnum;

import javax.persistence.Id;

public class ChatMessageDto {
    @Id
    private String id;
    //用户id
    private Integer uid;

    private String groupId;

    private Integer status = 1;//未发送成功，默认：1发送成功但未接受，2发送成功并成功接收

    //信息格式
    private MessageTypeEnum messageType;

    //内容
    private String content;

    //创建时间
    private long createTime;
}
