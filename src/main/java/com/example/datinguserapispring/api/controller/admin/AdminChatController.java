package com.example.datinguserapispring.api.controller.admin;


import com.example.datinguserapispring.dto.chat.*;
import com.example.datinguserapispring.dto.chat.request.ChatRequest;
import com.example.datinguserapispring.dto.chat.response.UnreadMessagesCountsResponse;
import com.example.datinguserapispring.security.SecurityContextService;
import com.example.datinguserapispring.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/chats")
public class AdminChatController {

    private final ChatService chatService;
    private final SecurityContextService securityContextService;


    @PostMapping
    public AdminSortedChat findAllChats(@RequestBody ChatCategory chatCategory,
                                        Pageable pageable) {
        String login = securityContextService.getUserDetails().getUsername();
        return chatService.findAllAdminChats(chatCategory, login, pageable);
    }

    @GetMapping("/find/by/{name}")
    public AdminSortedChat findNameFromChats(@PathVariable String name, Pageable pageable) {
        String login = securityContextService.getUserDetails().getUsername();
        return chatService.findNameFromChats(login, pageable, name);
    }

    @PostMapping("/messages")
    public List<MessageDTO> findChatMessagesByChatId(@RequestBody ChatRequest chatId,
                                                     Pageable pageable) {
        String login = securityContextService.getUserDetails().getUsername();
        return chatService.findChatMessagesAdmin(chatId, pageable, login);
    }

    @PostMapping("/unread-messages")
    public UnreadMessagesCountsResponse findAllUnreadChatMessages(Pageable pageable) {
        String login = securityContextService.getUserDetails().getUsername();
        return chatService.findAllUnreadMessages(login,pageable);
    }
}
