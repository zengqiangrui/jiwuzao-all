package com.jiwuzao.common.domain.mongo.entity;

import com.jiwuzao.common.domain.enumType.OnlineStatusEnum;
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
public class ChatGroup {
    @Id
    private String id;

    private Integer uidA;//初始发起聊天者的id

    private OnlineStatusEnum onlineStatusA = OnlineStatusEnum.OFF_LINE;//A的在线情况,默认不在线

    private Integer uidB;//初始被发起聊天者的id

    private OnlineStatusEnum onlineStatusB = OnlineStatusEnum.OFF_LINE;//B在线情况

    private Long createTime;

}
