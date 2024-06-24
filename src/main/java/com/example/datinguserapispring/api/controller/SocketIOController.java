package com.example.datinguserapispring.api.controller;

import com.example.datinguserapispring.dto.chat.request.ChatCreateRequest;
import com.example.datinguserapispring.dto.chat.response.ChatResponse;
import com.example.datinguserapispring.dto.chat.request.StartChatWhenSearchRequest;
import com.example.datinguserapispring.security.SecurityContextService;
import com.example.datinguserapispring.service.chat.ChatStartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat/start")
@RequiredArgsConstructor
public class SocketIOController {


    private final ChatStartService chatStartService;
    private final SecurityContextService securityContextService;

    @PostMapping
    public ChatResponse register(@RequestBody ChatCreateRequest chatCreateRequest) {
        String id = securityContextService.getUserDetails().getUsername();
        return chatStartService.registerChat(chatCreateRequest, id);
    }

    @PostMapping("/bot")
    public ChatResponse register(@RequestBody StartChatWhenSearchRequest chatCreateRequest) {
        String id = securityContextService.getUserDetails().getUsername();
        return chatStartService.registerChatWithBot(chatCreateRequest, id);
    }
}
