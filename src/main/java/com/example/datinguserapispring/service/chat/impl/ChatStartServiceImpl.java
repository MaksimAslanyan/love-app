package com.example.datinguserapispring.service.chat.impl;


import com.example.datinguserapispring.dto.chat.request.ChatCreateRequest;
import com.example.datinguserapispring.dto.chat.request.StartChatWhenSearchRequest;
import com.example.datinguserapispring.dto.chat.response.ChatResponse;
import com.example.datinguserapispring.exception.AuthenticationException;
import com.example.datinguserapispring.exception.Error;
import com.example.datinguserapispring.exception.UserNotFoundException;
import com.example.datinguserapispring.listener.SocketEventTracker;
import com.example.datinguserapispring.model.entity.chat.Chat;
import com.example.datinguserapispring.model.entity.chat.ChatMember;
import com.example.datinguserapispring.model.entity.chat.ChatMemberAdmin;
import com.example.datinguserapispring.model.entity.user.User;
import com.example.datinguserapispring.model.enums.ChatType;
import com.example.datinguserapispring.repository.Bot2Repository;
import com.example.datinguserapispring.repository.ChatMemberAdminRepository;
import com.example.datinguserapispring.repository.ChatMemberRepository;
import com.example.datinguserapispring.repository.UserRepository;
import com.example.datinguserapispring.service.bot.GenerateBotService;
import com.example.datinguserapispring.service.chat.ChatService;
import com.example.datinguserapispring.service.chat.ChatStartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
@Log4j2
@RequiredArgsConstructor
public class ChatStartServiceImpl implements ChatStartService {


    private final UserRepository userRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final ChatMemberAdminRepository chatMemberAdminRepository;
    private final Bot2Repository bot2Repository;
    private final ChatService chatService;
    private final GenerateBotService generateBotService;
    private final SocketEventTracker socketEventTracker;
    private final AbstractChatStartServiceHelper abstractChatStartServiceHelper;


    @Override
    public ChatResponse registerChat(ChatCreateRequest chatCreateRequest, String id) {
        var isMatch = false;
        return registerChatCommon(chatCreateRequest, LocalDateTime.now(), isMatch, id);
    }

    @Override
    public void createChatWhenLikeBack(ChatCreateRequest chatCreateRequest, LocalDateTime likedBackTime) {
        var isMatch = true;
        registerChatCommon(chatCreateRequest, likedBackTime, isMatch, null);
    }

    @Override
    public ChatResponse registerChatWithBot(StartChatWhenSearchRequest chatCreateRequest, String id) {
        String userId = chatCreateRequest.getFirstUserId();
        String secondBotId = chatCreateRequest.getSecondUserId();
        var isMatch = false;
        boolean isExist = bot2Repository.existsById(secondBotId);
        if (isExist){
            isMatch = true;
        }

        if (!isExist){
            generateBotService.generate(chatCreateRequest, id);
        }

        ChatCreateRequest request = ChatCreateRequest.builder()
                .firstUserId(userId)
                .secondUserId(secondBotId)
                .build();
        return registerChatCommon(request, LocalDateTime.now(), isMatch, id);
    }

    @Override
    public void createChatWhenUserLike(ChatCreateRequest chatCreateRequest) {
        var isMatch = true;
        registerChatCommon(chatCreateRequest, LocalDateTime.now(), isMatch, null);
    }

    private ChatResponse registerChatCommon(ChatCreateRequest chatCreateRequest,
                                            LocalDateTime likedBackTime,
                                            boolean isMatch,
                                            String id) {
        log.info("Entering register method");

        User firstUser = userRepository.findById(chatCreateRequest.getFirstUserId())
                .orElseThrow(() -> new UserNotFoundException(Error.USER_NOT_FOUND));
        log.info("Find first User {}: ", firstUser.getId());

        Optional<String> chatIdOptional = abstractChatStartServiceHelper.findCommonChatId(chatCreateRequest, firstUser);

        String chatId;
        String eventKey = chatCreateRequest.getFirstUserId() + "_" + chatCreateRequest.getSecondUserId();

        if (chatIdOptional.isPresent()) {
            chatId = chatIdOptional.get();
            socketEventTracker.createSocketEvent(eventKey);

            log.info("Found existing chat with ID: " + chatId);
        } else {
            chatId = createOrSaveChat(chatCreateRequest, likedBackTime, isMatch, firstUser, eventKey);
        }

        return new ChatResponse(chatId);
    }

    private String createOrSaveChat(ChatCreateRequest chatCreateRequest,
                                    LocalDateTime likedBackTime,
                                    boolean isMatch,
                                    User firstUser,
                                    String eventKey) {
        if (userRepository.existsById(chatCreateRequest.getSecondUserId())) {
            socketEventTracker.createSocketEvent(eventKey);
            Chat savedChat = chatService.save(new Chat(likedBackTime, ChatType.NULL, likedBackTime, isMatch));
            chatMemberRepository.save(new ChatMember(savedChat, firstUser));
            chatMemberRepository.save(new ChatMember(savedChat, new User()));
            log.info("Members and chat saved successfully");

            return savedChat.getId();
        } else if (bot2Repository.existsById(chatCreateRequest.getSecondUserId())) {

            return createChatWithBot2AndAdmin(
                    chatCreateRequest.getSecondUserId(),
                    firstUser,
                    eventKey,
                    isMatch,
                    likedBackTime);
        } else {
            throw new UserNotFoundException(Error.USER_NOT_FOUND);
        }
    }

    public String createChatWithBot2AndAdmin(String secondUserId,
                                             User firstUser,
                                             String eventKey,
                                             boolean isMatch,
                                             LocalDateTime likedBackTime) {
        if (isMatch) {
            return createMatchedChat(firstUser, eventKey, secondUserId, likedBackTime);
        } else {
            return createNonMatchedChat(secondUserId, firstUser, eventKey);
        }
    }

    private String createMatchedChat(User firstUser,
                                     String eventKey,
                                     String secondUserId,
                                     LocalDateTime likedBackTime) {
        var bot2 = bot2Repository.findById(secondUserId)
                .orElseThrow(() -> new AuthenticationException(Error.USER_NOT_FOUND));
        socketEventTracker.createSocketEvent(eventKey);
        var savedChat = chatService
                .save(new Chat(likedBackTime, ChatType.NULL, likedBackTime, true));
        chatMemberRepository.save(new ChatMember(savedChat, firstUser));
        chatMemberAdminRepository.save(new ChatMemberAdmin(savedChat, bot2));
        log.info("Matched chat: " + savedChat.getId());
        return savedChat.getId();
    }

    private String createNonMatchedChat(String secondUserId, User firstUser, String eventKey) {
        var bot2 = bot2Repository.findById(secondUserId)
                .orElseThrow(() -> new AuthenticationException(Error.USER_NOT_FOUND));

        var admin = bot2.getAdmin();
        log.info("Bot2 object: " + bot2);
        log.info("Admin object: " + admin);
        var savedChat = chatService
                .save(new Chat(LocalDateTime.now(), ChatType.NULL, LocalDateTime.now()));

        chatMemberAdminRepository
                .save(new ChatMemberAdmin(savedChat, bot2));
        chatMemberRepository
                .save(new ChatMember(savedChat, firstUser));

        socketEventTracker.createSocketEvent(eventKey);
        log.info("END creating object: " + savedChat.getId());
        return savedChat.getId();
    }



}