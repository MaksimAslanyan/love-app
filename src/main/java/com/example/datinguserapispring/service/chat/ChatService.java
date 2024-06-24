package com.example.datinguserapispring.service.chat;

import com.example.datinguserapispring.dto.chat.request.ChatRequest;
import com.example.datinguserapispring.dto.chat.response.ChatResponse;
import com.example.datinguserapispring.dto.chat.response.ProfileSnapshotChatResponse;
import com.example.datinguserapispring.dto.chat.response.UnreadMessagesCountsResponse;
import com.example.datinguserapispring.dto.user.response.ProfileSnapshotSearchResponse;
import com.example.datinguserapispring.dto.chat.*;
import com.example.datinguserapispring.model.entity.chat.Chat;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChatService {

    Chat save(Chat chat);

    List<ProfileSnapshotSearchResponse> findMostFrequentChats(String id);

    List<MessageDTO> findChatMessagesAdmin(ChatRequest chatId, Pageable pageable, String userId);

    List<ProfileSnapshotChatResponse> findAllChats(String id, Pageable pageable);

    ChatResponse deleteChat(String chatId);

    AdminSortedChat findAllAdminChats(ChatCategory chatCategory, String login, Pageable pageable);

    AdminSortedChat findNameFromChats(String login, Pageable pageable, String name);

    UnreadMessagesCountsResponse findAllUnreadMessages(String login, Pageable pageable);

    List<MessageDTO> findChatMessagesUser(ChatRequest chatId, Pageable pageable, String id);

    UnreadMessagesCountDTO convertToUnreadMessagesCountDTOList(List<Object[]> resultList);
}
