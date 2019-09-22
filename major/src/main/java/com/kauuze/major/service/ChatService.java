package com.kauuze.major.service;

import com.jiwuzao.common.domain.enumType.MessageTypeEnum;
import com.jiwuzao.common.domain.enumType.OnlineStatusEnum;
import com.jiwuzao.common.domain.mongo.entity.ChatGroup;
import com.jiwuzao.common.domain.mongo.entity.ChatMessage;
import com.jiwuzao.common.domain.mongo.entity.userBastic.UserInfo;
import com.jiwuzao.common.dto.chat.ChatGroupDto;
import com.jiwuzao.common.dto.chat.ChatMessageDto;
import com.jiwuzao.common.include.JsonUtil;
import com.jiwuzao.common.include.StringUtil;
import com.kauuze.major.domain.mongo.repository.ChatMessageRepository;
import com.kauuze.major.domain.mongo.repository.ChatGroupRepository;
import com.kauuze.major.domain.mongo.repository.UserInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ChatService {
    @Autowired
    private ChatGroupRepository chatGroupRepository;
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

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
            return transToDto(chatGroupRepository.save(chatGroup));
        }
    }

    /**
     * 获取用户所有好友(可聊天对象)
     *
     * @param uid
     * @return
     */
    public List<ChatGroupDto> getUserAllGroup(int uid) {
        List<ChatGroup> chatGroups = chatGroupRepository.findByUidAOrUidB(uid, uid);
        List<ChatGroupDto> chatGroupDtos = new ArrayList<>();
        for (ChatGroup chatGroup : chatGroups) {
            UserInfo userA = userInfoRepository.findByUid(chatGroup.getUidA());
            UserInfo userB = userInfoRepository.findByUid(chatGroup.getUidB());
            if(null==userA||null==userB)
                throw new RuntimeException("未找到分组");
            ChatGroupDto chatGroupDto = new ChatGroupDto()
                    .setUidA(chatGroup.getUidA())
                    .setUidB(chatGroup.getUidB())
                    .setGroupId(chatGroup.getId())
                    .setUserNameA(userA.getNickName())
                    .setUserNameB(userB.getNickName())
                    .setAvatarA(userA.getPortrait())
                    .setAvatarB(userB.getPortrait())
                    .setOnlineStatusA(chatGroup.getOnlineStatusA())
                    .setOnlineStatusB(chatGroup.getOnlineStatusB())
                    .setSex(uid == userA.getUid() ? userB.getSex() : userA.getSex())
                    .setUndoNum(getUserGroupUndoNum(uid, chatGroup.getId()));
            chatGroupDtos.add(chatGroupDto);
        }
        return chatGroupDtos;
    }

    /**
     * 获取用户某个聊天组中未处理信息数
     *
     * @param uid     用户id
     * @param groupId 聊天组id
     * @return 整数
     */
    public Integer getUserGroupUndoNum(int uid, String groupId) {
        return chatMessageRepository.countByGroupIdAndStatusAndUidIsNot(groupId, 1, uid);//1表示对方成功发送，自己未处理(回复，删除表示处理)
    }

    /**
     * 获取用户所有未处理信息数量
     *
     * @param uid
     * @return
     */
    public Integer getUserAllUndoNum(int uid) {
        List<ChatGroup> list = chatGroupRepository.findByUidAOrUidB(uid, uid);
        Integer res = 0;
        for (ChatGroup chatGroup : list) {
            res += getUserGroupUndoNum(uid, chatGroup.getId());
        }
        return res;
    }


    /**
     * 用户修改在group中的状态
     *
     * @param uid
     * @param groupId
     * @param status
     * @return
     */
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
     * @return
     */
    @Async
    public Future<?> createChatMessage(String message) {
        // 消息持久化,使用线程池并发执行
        return threadPoolTaskExecutor.submit(() -> {
            ChatMessage chatMessage = JsonUtil.parseJsonString(message, ChatMessage.class);
            chatMessage.setStatus(1)
                    .setCreateTime(System.currentTimeMillis());
            ChatMessage save = chatMessageRepository.save(chatMessage);
            if (null != save) {
                ChatMessageDto chatMessageDto = new ChatMessageDto();
                BeanUtils.copyProperties(save, chatMessageDto);
                return chatMessageDto;
            } else {
                throw new RuntimeException("消息入库异常");
            }
        });
    }

    /**
     * 处理用户未读信息
     * 用户读取和删除时，将信息处理
     * status 1未处理，2已处理
     *
     * @param groupId 聊天组id
     * @param uid     用户id
     * @return future
     */
    @Async
    public Future<?> handleAllUndoMessage(String groupId, int uid) {
        return threadPoolTaskExecutor.submit(() -> {
            List<ChatMessage> collect = chatMessageRepository.findAllByGroupIdAndStatusAndUidIsNot(groupId, 1, uid)
                    .stream().map(chatMessage -> chatMessage.setStatus(2)
                    ).collect(Collectors.toList());
            chatMessageRepository.saveAll(collect);
        });
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
            BeanUtils.copyProperties(chatMessage, chatMessageDto,"id");
            list.add(chatMessageDto);
        }
        return list;
    }


    public void checkMessageTask(List<Future> futures) throws ExecutionException, InterruptedException {
        //查询任务执行的结果
        for (Future<?> future : futures) {
            if (!future.isCancelled() && future.isDone())
                log.info("Future未处理有:{}", future.get());
        }
    }

    /**
     * 将对象转换
     *
     * @param chatGroup
     * @return
     */
    private ChatGroupDto transToDto(ChatGroup chatGroup) {
        System.out.println(chatGroup);
        UserInfo userA = userInfoRepository.findByUid(chatGroup.getUidA());
        UserInfo userB = userInfoRepository.findByUid(chatGroup.getUidB());
        return new ChatGroupDto().setAvatarA(userA.getPortrait()).setAvatarB(userB.getPortrait())
                .setUidA(userA.getUid()).setUidB(userB.getUid()).setUserNameA(userA.getNickName()).setUserNameB(userB.getNickName())
                .setGroupId(chatGroup.getId());
    }


    public ChatGroupDto getOfficialChat(int uid) {

        return null;
    }
}
