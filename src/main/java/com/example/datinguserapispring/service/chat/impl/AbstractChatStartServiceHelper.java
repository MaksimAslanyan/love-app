package com.example.datinguserapispring.service.chat.impl;

import com.example.datinguserapispring.dto.chat.request.ChatCreateRequest;
import com.example.datinguserapispring.dto.chat.request.MessageRequest;
import com.example.datinguserapispring.model.entity.bot.Bot2;
import com.example.datinguserapispring.model.entity.chat.Chat;
import com.example.datinguserapispring.model.entity.user.User;

import java.util.Optional;

abstract class AbstractChatStartServiceHelper {


    abstract void checkIfFirstChatMessage(MessageRequest message, User user, Bot2 receiverBotId, Chat chat);

    abstract boolean isMessageSenderUserAndBot(MessageRequest message, User user,
                                               Bot2 bot2, User receiverId,
                                               Bot2 receiverBotId, Chat chat,
                                               boolean senderMessageSaved);

    abstract boolean isMessageSenderBotAndUser(MessageRequest message, User user,
                                               Bot2 bot2, User receiverId,
                                               Bot2 receiverBotId, Chat chat,
                                               boolean senderMessageSaved);


    abstract Optional<String> findCommonChatId(ChatCreateRequest chatCreateRequest, User firstUser);
}
