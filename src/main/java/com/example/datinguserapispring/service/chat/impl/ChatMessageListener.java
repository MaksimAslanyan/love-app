package com.example.datinguserapispring.service.chat.impl;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.example.datinguserapispring.dto.admin.AdminActionLogDTO;
import com.example.datinguserapispring.dto.chat.request.MessageRequest;
import com.example.datinguserapispring.exception.EntityNotFoundException;
import com.example.datinguserapispring.exception.Error;
import com.example.datinguserapispring.model.entity.Admin;
import com.example.datinguserapispring.model.entity.bot.Bot2;
import com.example.datinguserapispring.model.entity.chat.Chat;
import com.example.datinguserapispring.model.entity.user.User;
import com.example.datinguserapispring.model.enums.ActionType;
import com.example.datinguserapispring.repository.*;
import com.example.datinguserapispring.service.admin.AdminActionService;
import com.example.datinguserapispring.service.fcmmessaging.FirebaseService;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Log4j2
@Component
public class ChatMessageListener implements DataListener<MessageRequest> {


    private static final ConnectListener ON_USER_CONNECT_WITH_SOCKET =
            client -> client.sendEvent("connectionResponse",
                    "Hello! You are connected to the Socket.IO server.");

    private static final DisconnectListener ON_USER_DISCONNECT_WITH_SOCKET =
            client -> log.info("Perform operation on user disconnect in controller");


    private final UserRepository userRepository;
    private final Bot2Repository bot2Repository;
    private final ChatRepository chatRepository;
    private final BlockRepository blockRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final AbstractChatStartServiceHelper abstractChatStartServiceHelper;
    private final FirebaseService firebaseService;
    private final SocketIOServer socketServer;
    private final AdminActionService adminActionService;


    public ChatMessageListener(UserRepository userRepository,
                               Bot2Repository bot2Repository,
                               ChatRepository chatRepository,
                               BlockRepository blockRepository,
                               ChatMessageRepository chatMessageRepository, AbstractChatStartServiceHelper abstractChatStartServiceHelper,
                               FirebaseService firebaseService,
                               SocketIOServer socketServer, AdminActionService adminActionService) {
        this.userRepository = userRepository;
        this.bot2Repository = bot2Repository;
        this.chatRepository = chatRepository;
        this.blockRepository = blockRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.abstractChatStartServiceHelper = abstractChatStartServiceHelper;
        this.firebaseService = firebaseService;
        this.socketServer = socketServer;
        this.adminActionService = adminActionService;
        this.socketServer.addConnectListener(ON_USER_CONNECT_WITH_SOCKET);
        this.socketServer.addDisconnectListener(ON_USER_DISCONNECT_WITH_SOCKET);
    }


    @Override
    public void onData(SocketIOClient client, MessageRequest message, AckRequest acknowledge) {

        boolean isBlocked = blockRepository
                .existsByBlockingUserIdAndBlockedBotId(message.getReceiverId(), message.getSenderId());

            if (!isBlocked) {

                socketServer.getBroadcastOperations().sendEvent(message.getTargetUserName(), client, message);


                User user = userRepository
                        .findById(message.getSenderId()).orElse(null);
                Bot2 bot2 = bot2Repository
                        .findById(message.getSenderId()).orElse(null);

                User receiverId = userRepository
                        .findById(message.getReceiverId()).orElse(null);
                Bot2 receiverBotId = bot2Repository
                        .findById(message.getReceiverId()).orElse(null);



                Chat chat = chatRepository
                        .findById(message.getChatId()).orElseThrow();

                isUserAndBot2NotNull(bot2, receiverId, chat);

                boolean senderMessageSaved = false;

                senderMessageSaved = abstractChatStartServiceHelper.isMessageSenderUserAndBot(
                        message, user, bot2,
                        receiverId, receiverBotId,
                        chat, senderMessageSaved);

                abstractChatStartServiceHelper.isMessageSenderBotAndUser(message, user, bot2,
                        receiverId, receiverBotId, chat,
                        senderMessageSaved);

                log.info(message.getSenderId() + " user send message to user " +
                        message.getReceiverId() + " and message is " +
                        message.getMessage());

                acknowledge.sendAckData("Message sent to the target user successfully");


                firebaseService.sendNotificationToUser(receiverId, bot2.getNameBot());
            }


        if (isBlocked) {
            acknowledge.sendAckData("You were block");
            log.info("You were block");
        }
    }

    @Async
    public void logAdminAction(Bot2 bot2, User user, Chat chat) {
        Bot2 bot = bot2Repository.findById(bot2.getId())
                .orElseThrow(() -> new EntityNotFoundException(Error.ENTITY_NOT_FOUND));
        Admin admin = bot.getAdmin();

        long messageCount = chatMessageRepository
                .countByChatIdAndChatMemberAdminBot2(chat.getId(), bot);

        isFirstBotMessage(user, admin, messageCount);
    }

    private void isUserAndBot2NotNull(Bot2 bot2, User receiverId, Chat chat) {
        if (bot2 != null && receiverId != null) {
            logAdminAction(bot2, receiverId, chat);
        }
    }

    private void isFirstBotMessage(User user, Admin admin, long messageCount) {
        if (messageCount <= 0) {
            adminActionService.saveAdminActionLog(AdminActionLogDTO.builder()
                    .user(user)
                    .admin(admin)
                    .createdAt(LocalDateTime.now())
                    .actionType(ActionType.valueOf(ActionType.DIALOGUE_START.getValue()))
                    .build());
        }
    }
}
