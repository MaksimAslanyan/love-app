package com.example.datinguserapispring.repository;

import com.example.datinguserapispring.dto.chat.AdminChatDTO;
import com.example.datinguserapispring.dto.chat.ChatCategory;
import com.example.datinguserapispring.dto.chat.ChatDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SearchChatRepository {

    List<AdminChatDTO> findUnreadMessages(String adminId, Pageable pageable);

}
