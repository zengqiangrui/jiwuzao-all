package com.kauuze.major.service;

import com.jiwuzao.common.domain.mongo.entity.ChatGroup;
import com.jiwuzao.common.domain.mongo.entity.userBastic.UserInfo;
import com.jiwuzao.common.dto.chat.ChatGroupDto;
import com.kauuze.major.domain.mongo.repository.ChatGroupRepository;
import com.kauuze.major.domain.mongo.repository.UserInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ChatService {
    @Autowired
    private ChatGroupRepository chatGroupRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    public ChatGroupDto initChatGroup(Integer uidA, Integer uidB){
        if(uidA.equals(uidB)){//如果是自己和自己就不会建立联系
            return null;
        }
        //验证是否存在聊天关系
        Optional<ChatGroup> optional = chatGroupRepository.getByUidAAndUidB(uidA, uidB);
        Optional<ChatGroup> optional1 = chatGroupRepository.getByUidAAndUidB(uidB, uidA);
        //如果有聊天关系就返回group对象
        if(optional.isPresent()){
            ChatGroup chatGroup = optional.get();
            return transToDto(chatGroup);
        }else if(optional1.isPresent()){
            ChatGroup chatGroup = optional1.get();
            return transToDto(chatGroup);
        }else{
            //没有聊天关系就建立一个
            ChatGroup chatGroup = new ChatGroup().setUidA(uidA)
                    .setUidB(uidB).setCreateTime(System.currentTimeMillis());
            chatGroup = chatGroupRepository.save(chatGroup);
            return transToDto(chatGroup);
        }
    }

    private ChatGroupDto transToDto(ChatGroup chatGroup){
        System.out.println(chatGroup);
        UserInfo userA = userInfoRepository.findByUid(chatGroup.getUidA());
        UserInfo userB = userInfoRepository.findByUid(chatGroup.getUidB());
        return new ChatGroupDto().setAvatarA(userA.getPortrait()).setAvatarB(userB.getPortrait())
                .setUidA(userA.getUid()).setUidB(userB.getUid()).setUserNameA(userA.getNickName()).setUserNameB(userB.getNickName())
                .setGroupId(chatGroup.getId());
    }



}
