package com.kauuze.major.service;

import com.jiwuzao.common.domain.enumType.MessageTypeEnum;
import com.jiwuzao.common.domain.enumType.OnlineStatusEnum;
import com.jiwuzao.common.domain.mongo.entity.ChatGroup;
import com.jiwuzao.common.domain.mongo.entity.ChatMessage;
import com.jiwuzao.common.domain.mongo.entity.userBastic.UserInfo;
import com.jiwuzao.common.dto.chat.ChatGroupDto;
import com.jiwuzao.common.dto.chat.ChatMessageDto;
import com.kauuze.major.domain.mongo.ChatMessageRepository;
import com.kauuze.major.domain.mongo.repository.ChatGroupRepository;
import com.kauuze.major.domain.mongo.repository.UserInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
@Slf4j
@Async
public class ChatService {
    @Autowired
    private ChatGroupRepository chatGroupRepository;
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private List<Future> futures = new ArrayList<>();

    /**
     * 建立聊天联系
     *
     * @param uidA
     * @param uidB
     * @return
     */
    public ChatGroupDto initChatGroup(Integer uidA, Integer uidB) {
        if (uidA.equals(uidB)) {//如果是自己和自己就不会建立联系
            throw new RuntimeException("不会跟自己建立联系");
        }
        //验证是否存在聊天关系
        Optional<ChatGroup> optional = chatGroupRepository.getByUidAAndUidB(uidA, uidB);
        Optional<ChatGroup> optional1 = chatGroupRepository.getByUidAAndUidB(uidB, uidA);
        //如果有聊天关系就返回group对象
        if (optional.isPresent()) {
            ChatGroup chatGroup = optional.get();
            return transToDto(chatGroup);
        } else if (optional1.isPresent()) {
            ChatGroup chatGroup = optional1.get();
            return transToDto(chatGroup);
        } else {
            //没有聊天关系就建立一个
            ChatGroup chatGroup = new ChatGroup().setUidA(uidA)
                    .setUidB(uidB).setCreateTime(System.currentTimeMillis());
            chatGroup = chatGroupRepository.save(chatGroup);
            return transToDto(chatGroup);
        }
    }

    public ChatGroupDto switchUserGroupStatus(int uid, String groupId, OnlineStatusEnum status) {
        Optional<ChatGroup> opt = chatGroupRepository.findById(groupId);
        if (!opt.isPresent()) {
            throw new RuntimeException("聊天关系未建立");
        } else {
            ChatGroup chatGroup = opt.get();
            if (uid == chatGroup.getUidA()) {
                chatGroup.setOnlineStatusA(status);
            } else {
                chatGroup.setOnlineStatusB(status);
            }
            return transToDto(chatGroupRepository.save(chatGroup));
        }
    }


    /**
     * 生成一条发送成功的信息
     *
     * @param groupId
     * @param uid
     * @param content
     * @param type
     * @return
     */
    public ChatMessageDto createChatMessage(String groupId, int uid, String content, MessageTypeEnum type) {
        // 消息持久化,使用线程池并发执行
        Future<?> future = threadPoolTaskExecutor.submit(() -> {
            ChatMessage chatMessage = new ChatMessage()
                    .setMessageType(type).setGroupId(groupId).setUid(uid)
                    .setContent(content).setCreateTime(System.currentTimeMillis());
            ChatMessage save = chatMessageRepository.save(chatMessage);
            if (null != save) {
                ChatMessageDto chatMessageDto = new ChatMessageDto();
                BeanUtils.copyProperties(save, chatMessageDto);
                return chatMessageDto;
            } else {
                throw new RuntimeException("消息入库异常");
            }
        });
        futures.add(future);
        return null;
    }

    /**
     * 分页获取group消息
     *
     * @param groupId
     * @return
     */
    public List<ChatMessageDto> getChatMessageByGroup(String groupId, Pageable pageable) {
        List<ChatMessage> allByGroupId = chatMessageRepository.findAllByGroupId(groupId, pageable).getContent();
        List<ChatMessageDto> list = new ArrayList<>();
        for (ChatMessage chatMessage : allByGroupId) {
            ChatMessageDto chatMessageDto = new ChatMessageDto();
            BeanUtils.copyProperties(chatMessage, chatMessageDto);
            list.add(chatMessageDto);
        }
        return list;
    }


    public void checkMessageTask() throws ExecutionException, InterruptedException {
        //查询任务执行的结果
        for (Future<?> future : futures) {
            if (!future.isCancelled() && future.isDone())
                System.out.println(future.get());
        }
    }


    private ChatGroupDto transToDto(ChatGroup chatGroup) {
        System.out.println(chatGroup);
        UserInfo userA = userInfoRepository.findByUid(chatGroup.getUidA());
        UserInfo userB = userInfoRepository.findByUid(chatGroup.getUidB());
        return new ChatGroupDto().setAvatarA(userA.getPortrait()).setAvatarB(userB.getPortrait())
                .setUidA(userA.getUid()).setUidB(userB.getUid()).setUserNameA(userA.getNickName()).setUserNameB(userB.getNickName())
                .setGroupId(chatGroup.getId());
    }

}
