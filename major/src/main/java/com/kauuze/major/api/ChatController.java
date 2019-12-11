package com.kauuze.major.api;

import com.jiwuzao.common.config.contain.SpringContext;
import com.jiwuzao.common.domain.enumType.MessageTypeEnum;
import com.jiwuzao.common.domain.enumType.OnlineStatusEnum;
import com.jiwuzao.common.dto.chat.ChatGroupDto;
import com.jiwuzao.common.dto.chat.ChatMessageDto;
import com.jiwuzao.common.include.JsonResult;
import com.jiwuzao.common.include.StringUtil;
import com.jiwuzao.common.pojo.chat.ChatGroupPojo;
import com.jiwuzao.common.pojo.common.UidPojo;
import com.jiwuzao.common.vo.chat.ChatGroupItemVO;
import com.kauuze.major.config.permission.Authorization;
import com.kauuze.major.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@RestController
@ServerEndpoint("/chat/{uid}/{groupId}")
@RequestMapping("/chatControl")
@Slf4j
public class ChatController {

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<ChatController> webSocketSet = new CopyOnWriteArraySet<>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    //接收uid
    private String uid = "";
    private String groupId = "";

    //用于延时处理信息入库的list
//    private List<Future> futures = new ArrayList<>();

    @Autowired
    private ChatService chatService;

    /**
     * 初始化聊天对象
     *
     * @param uid
     * @param uidB
     * @return
     */
    @RequestMapping("/init")
    @Authorization
    public JsonResult initChat(@RequestAttribute int uid, @Valid @RequestBody UidPojo uidB) {
        ChatGroupDto chatGroupDto = chatService.initChatGroup(uid, uidB.getUid());
//        ChatGroupItemVO chatGroupItemVO = new ChatGroupItemVO();
        if (null != chatGroupDto) {
            return JsonResult.success(chatGroupDto);
        } else {
            return JsonResult.failure("建立聊天失败");
        }
    }

    /**
     * 获取用户所有聊天对象列表
     *
     * @param uid
     * @return
     */
    @RequestMapping("/getAllGroup")
    @Authorization
    public JsonResult getAllGroup(@RequestAttribute int uid) {
        List<ChatGroupDto> userAllGroup = chatService.getUserAllGroup(uid);
        List<ChatGroupItemVO> collect = userAllGroup.stream().map(chatGroupDto -> createChatItem(uid, chatGroupDto))
                .sorted(Comparator.comparing(ChatGroupItemVO::getUndoNum).reversed())//根据未处理的数量进行降序排列
                .collect(Collectors.toList());
        return JsonResult.success(collect);
    }

    private ChatGroupItemVO createChatItem(int uid, ChatGroupDto chatGroupDto) {
        return new ChatGroupItemVO()
                .setUid(chatGroupDto.getUidA() == uid ? chatGroupDto.getUidB() : chatGroupDto.getUidA())
                .setNickName(chatGroupDto.getUidA() == uid ? chatGroupDto.getUserNameB() : chatGroupDto.getUserNameA())
                .setAvatar(chatGroupDto.getUidA() == uid ? chatGroupDto.getAvatarB() : chatGroupDto.getAvatarA())
                .setOnlineStatus(chatGroupDto.getUidA() == uid ? chatGroupDto.getOnlineStatusB() : chatGroupDto.getOnlineStatusA())
                .setGroupId(chatGroupDto.getGroupId()).setUndoNum(chatGroupDto.getUndoNum()).setSex(chatGroupDto.getSex());
    }

    @RequestMapping("/getOfficial")
    @Authorization
    public JsonResult getOfficialChat(@RequestAttribute int uid) {
        ChatGroupDto chatGroupDto = chatService.getOfficialChat(uid);
        if (null != chatGroupDto) {
            return JsonResult.success(createChatItem(uid, chatGroupDto));
        } else {
            return JsonResult.failure();
        }
    }

    /**
     * 获取用户未处理消息总数
     *
     * @param uid
     * @return
     */
    @RequestMapping("/getUserUndo")
    @Authorization
    public JsonResult getUserUndo(@RequestAttribute int uid) {
        Integer userAllUndoNum = chatService.getUserAllUndoNum(uid);
        return JsonResult.success(userAllUndoNum);
    }

    @RequestMapping("/handleMessage")
    @Authorization
    public JsonResult handleMessage(@RequestAttribute int uid, @Valid @RequestBody ChatGroupPojo pojo) {
        Future<?> future = chatService.handleAllUndoMessage(pojo.getGroupId(), uid);
        return JsonResult.success();
    }

    /**
     * 根据groupId 分页获取信息列表
     *
     * @param pojo
     * @return
     */
    @RequestMapping("/getGroupMessage")
    @Authorization
    public JsonResult getGroupMessage(@Valid @RequestBody ChatGroupPojo pojo) {
        List<ChatMessageDto> chatMessageByGroup = chatService.getChatMessageByGroup(pojo.getGroupId(), PageRequest.of(pojo.getPageNum(), pojo.getPageSize(),
                Sort.by(pojo.getIsAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, pojo.getCreateBy())
        ));
        return JsonResult.success(chatMessageByGroup);
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("uid") String uid, @PathParam("groupId") String groupId) throws IOException {
        this.session = session;
        this.uid = uid;
        this.groupId = groupId;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        log.info("有用户加入:" + uid + ",当前在线人数为" + getOnlineCount());
        final ChatService chatService = SpringContext.getBean(ChatService.class);
        chatService.switchUserGroupStatus(Integer.parseInt(uid), groupId, OnlineStatusEnum.ON_LINE);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        Map<String, List<String>> requestParameterMap = session.getRequestParameterMap();
        String uid = requestParameterMap.get("uid").get(0);
        String groupId = requestParameterMap.get("groupId").get(0);
        log.info("uid{},groupId{}", uid, groupId);
        log.info("收到来自窗口" + this.uid + "的信息:" + message);
        final ChatService chatService = SpringContext.getBean(ChatService.class);
        chatService.createChatMessage(message);
        //转发消息
        for (ChatController item : webSocketSet) {
            try {
                if (item.groupId.equals(groupId) && !item.uid.equals(uid))
                    item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
        final ChatService chatService = SpringContext.getBean(ChatService.class);
        chatService.switchUserGroupStatus(Integer.parseInt(uid), groupId, OnlineStatusEnum.OFF_LINE);
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    private void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

//    /**
//     * 指定消息发送
//     */
//    public static void sendInfo(String message, @PathParam("uid") String uid) throws IOException {
//        log.info("推送消息到窗口" + uid + "，推送内容:" + message);
//        for (ChatController item : webSocketSet)
//            try {
//                //这里可以设定只推送给这个uid的，为null则全部推送
//                if (uid == null) {
//                    item.sendMessage(message);
//                } else if (item.uid.equals(uid)) {
//                    item.sendMessage(message);
//                }
//            } catch (IOException e) {
//                continue;
//            }
//    }

    private static synchronized int getOnlineCount() {
        return onlineCount;
    }

    private static synchronized void addOnlineCount() {
        ChatController.onlineCount++;
    }

    private static synchronized void subOnlineCount() {
        ChatController.onlineCount--;
    }

}

