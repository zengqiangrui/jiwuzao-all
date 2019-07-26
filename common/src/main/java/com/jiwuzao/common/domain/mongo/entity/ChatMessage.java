package com.jiwuzao.common.domain.mongo.entity;

import com.jiwuzao.common.domain.enumType.MessageTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    @Id
    private String id;
    //用户id
    private Integer uid;

    private String groupId;

    private Integer status;//0未发送成功，1发送成功但未接受，2发送成功并成功接收

    //信息格式
    private MessageTypeEnum messageType;

    //内容
    private String content;

    //创建时间
    private long createTime;


}
