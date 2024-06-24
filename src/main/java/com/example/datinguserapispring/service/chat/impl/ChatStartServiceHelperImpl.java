package com.example.datinguserapispring.service.chat.impl;


import com.example.datinguserapispring.dto.chat.request.ChatCreateRequest;
import com.example.datinguserapispring.dto.chat.request.MessageRequest;
import com.example.datinguserapispring.model.entity.bot.Bot2;
import com.example.datinguserapispring.model.entity.chat.Chat;
import com.example.datinguserapispring.model.entity.chat.ChatMember;
import com.example.datinguserapispring.model.entity.chat.ChatMemberAdmin;
import com.example.datinguserapispring.model.entity.chat.ChatMessage;
import com.example.datinguserapispring.model.entity.user.User;
import com.example.datinguserapispring.model.enums.ChatType;
import com.example.datinguserapispring.model.enums.Gender;
import com.example.datinguserapispring.repository.ChatMemberAdminRepository;
import com.example.datinguserapispring.repository.ChatMemberRepository;
import com.example.datinguserapispring.repository.ChatMessageRepository;
import com.example.datinguserapispring.repository.ChatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
public class ChatStartServiceHelperImpl extends AbstractChatStartServiceHelper {

    private final ChatRepository chatRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final ChatMemberAdminRepository chatMemberAdminRepository;



    void checkIfFirstChatMessage(MessageRequest message, User user, Bot2 receiverBotId, Chat chat) {
        if (!message.isFirstChatMessage()) {
            sortChatByOrientation(user, receiverBotId, chat);
        }
    }

    boolean isMessageSenderBotAndUser(MessageRequest message, User user,
                                      Bot2 bot2, User receiverId,
                                      Bot2 receiverBotId, Chat chat,
                                      boolean senderMessageSaved) {
        if (receiverId != null && !receiverId.equals(user) && !senderMessageSaved) {
            checkIfFirstChatMessage(message, receiverId, bot2, chat);
            saveMessageForUser(receiverId, chat, message);

        } else if (receiverBotId != null && !receiverBotId.equals(bot2) && !senderMessageSaved) {
            checkIfFirstChatMessage(message, user, receiverBotId, chat);
            saveMessageForBot2(receiverBotId, chat, message);
        }
        return false;
    }

    boolean isMessageSenderUserAndBot(MessageRequest message, User user,
                                      Bot2 bot2, User receiverId,
                                      Bot2 receiverBotId, Chat chat,
                                      boolean senderMessageSaved) {
        if (user != null) {
            checkIfFirstChatMessage(message, user, receiverBotId, chat);
            saveMessageForUser(user, chat, message);
            senderMessageSaved = true;

        } else if (bot2 != null) {

            checkIfFirstChatMessage(message, receiverId, bot2, chat);
            saveMessageForBot2(bot2, chat, message);
            senderMessageSaved = true;
        }
        return senderMessageSaved;
    }

    Optional<String> findCommonChatId(ChatCreateRequest chatCreateRequest, User firstUser) {
        return findChatIdInLists(
                chatMemberRepository.findByUserId(firstUser.getId())
                        .orElse(Collections.emptyList()),
                chatMemberRepository.findByUserId(chatCreateRequest.getSecondUserId())
                        .orElse(Collections.emptyList()),
                chatMemberAdminRepository.findByBot2Id(chatCreateRequest.getSecondUserId())
                        .orElse(Collections.emptyList())
        );
    }

    private void saveMessageForBot2(Bot2 bot2, Chat chat, MessageRequest message) {
        ChatMemberAdmin chatMemberAdmin = chatMemberAdminRepository
                .findByChatIdAndBot2Id(chat.getId(), bot2.getId()).orElseThrow();
        chat.setLastActivity(LocalDateTime.now());
        ChatMessage chatMessage = ChatMessage.builder()
                .message(message.getMessage())
                .chat(chat)
                .chatMemberAdmin(chatMemberAdmin)
                .isRead(false)
                .photoId(message.getPhotoId())
                .canSee(true)
                .createdDate(LocalDateTime.now())
                .build();
        chatRepository.save(chat);
        chatMessageRepository.save(chatMessage);
    }

    private void saveMessageForUser(User user, Chat chat, MessageRequest message) {
        ChatMember chatMember = chatMemberRepository
                .findByChatIdAndUserId(chat.getId(), user.getId()).orElseThrow();

        chat.setLastActivity(LocalDateTime.now());
        ChatMessage chatMessage = ChatMessage.builder()
                .message(message.getMessage())
                .chat(chat)
                .chatMember(chatMember)
                .isRead(false)
                .photoId(message.getPhotoId())
                .canSee(true)
                .createdDate(LocalDateTime.now())
                .build();
        chatRepository.save(chat);
        chatMessageRepository.save(chatMessage);
    }

    private void sortChatByOrientation(User firstUser, Bot2 bot2, Chat chat) {
        Gender userGender = firstUser.getGender();
        Gender botGender = bot2.getBot1().getGender();
        boolean isPremium = firstUser.isPremium();


        if (userGender == Gender.FEMALE && botGender == Gender.FEMALE) {
            chat.setChatType(ChatType.LESBIAN);
        }
        if (userGender == Gender.MALE && botGender == Gender.MALE) {
            chat.setChatType(ChatType.GAY);
        }
        if (userGender == Gender.MALE && botGender == Gender.FEMALE) {
            chat.setChatType(ChatType.MALE_FEMALE);
        }
        if (userGender == Gender.FEMALE && botGender == Gender.MALE) {
            chat.setChatType(ChatType.FEMALE_MALE);
        }
        if (isPremium) {
            chat.setChatType(ChatType.IS_PREMIUM);
        }
        chatRepository.save(chat);
    }

    private Optional<String> findChatIdInLists(List<ChatMember> chatMembers,
                                               List<ChatMember> chatMembers2,
                                               List<ChatMemberAdmin> chatMemberAdmins) {
        Set<String> chatIds = Stream.concat(
                chatMembers.stream().map(chatMember -> chatMember.getChat().getId()),
                chatMembers2.stream().map(chatMember -> chatMember.getChat().getId())
        ).collect(Collectors.toSet());

        return chatMemberAdmins.stream()
                .filter(member -> chatIds.contains(member.getChat().getId()))
                .map(ChatMemberAdmin::getChat)
                .map(Chat::getId)
                .findFirst();
    }

}
