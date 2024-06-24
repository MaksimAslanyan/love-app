package com.example.datinguserapispring.api.controller;


import com.example.datinguserapispring.dto.chat.request.ChatRequest;
import com.example.datinguserapispring.dto.chat.response.ChatResponse;
import com.example.datinguserapispring.dto.chat.MessageDTO;
import com.example.datinguserapispring.dto.chat.response.ProfileSnapshotChatResponse;
import com.example.datinguserapispring.dto.user.response.ProfileSnapshotSearchResponse;
import com.example.datinguserapispring.security.SecurityContextService;
import com.example.datinguserapispring.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chats")
public class ChatController {


    private final ChatService chatService;
    private final SecurityContextService securityContextService;


    @GetMapping()
    public List<ProfileSnapshotChatResponse> findAllChatsById(Pageable pageable) {
        String id = securityContextService.getUserDetails().getUsername();
        return chatService.findAllChats(id, pageable);
    }

    @GetMapping("/most-frequent")
    public List<ProfileSnapshotSearchResponse> findMostFrequentChats() {
        String id = securityContextService.getUserDetails().getUsername();
        return chatService.findMostFrequentChats(id);
    }

    @PostMapping("/messages")
    public List<MessageDTO> findChatMessagesByChatId(@RequestBody ChatRequest chatId,
                                                     Pageable pageable) {
        String id = securityContextService.getUserDetails().getUsername();
        return chatService.findChatMessagesUser(chatId, pageable, id);
    }

    @DeleteMapping("/{chatId}")
    public ChatResponse findChatMessagesByChatId(@PathVariable String chatId) {
        return chatService.deleteChat(chatId);
    }

}
