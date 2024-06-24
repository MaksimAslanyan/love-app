package com.example.datinguserapispring.service.chat;

import com.example.datinguserapispring.dto.chat.request.ChatCreateRequest;
import com.example.datinguserapispring.dto.chat.response.ChatResponse;
import com.example.datinguserapispring.dto.chat.request.StartChatWhenSearchRequest;

import java.time.LocalDateTime;

public interface ChatStartService {
    ChatResponse registerChat(ChatCreateRequest chatCreateRequest,String id);

    void createChatWhenLikeBack(ChatCreateRequest chatCreateRequest, LocalDateTime likedBackTime);

    ChatResponse registerChatWithBot(StartChatWhenSearchRequest chatCreateRequest, String id);

    void createChatWhenUserLike(ChatCreateRequest chatCreateRequest);
}
