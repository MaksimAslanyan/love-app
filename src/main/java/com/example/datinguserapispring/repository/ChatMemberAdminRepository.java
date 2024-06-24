package com.example.datinguserapispring.repository;

import com.example.datinguserapispring.model.entity.chat.ChatMemberAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMemberAdminRepository extends
        JpaRepository<ChatMemberAdmin, String>,
        JpaSpecificationExecutor<ChatMemberAdmin> {

    Optional<ChatMemberAdmin> findByChatIdAndBot2Id(String chatId, String bot2Id);

    Optional<List<ChatMemberAdmin>> findByBot2Id(String bot2Id);

    List<ChatMemberAdmin> findChatMemberAdminsByChatId(String chatId);
}
