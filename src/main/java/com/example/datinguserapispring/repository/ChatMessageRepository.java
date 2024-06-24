package com.example.datinguserapispring.repository;

import com.example.datinguserapispring.model.entity.bot.Bot2;
import com.example.datinguserapispring.model.entity.chat.Chat;
import com.example.datinguserapispring.model.entity.chat.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, String>, JpaSpecificationExecutor<ChatMessage> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE dating_user_api.chat_message SET is_read = true WHERE id = ?1 ", nativeQuery = true)
    void setIsReadTrue(String messageId);


    Page<ChatMessage> findByChatIdOrderByCreatedDateDesc(String chatId, Pageable pageable);

    Optional<ChatMessage> findFirstByChatIdOrderByCreatedDateDesc(String chatId);

    @Query(value = "SELECT COUNT(cm) FROM dating_user_api.chat_message cm " +
            "WHERE cm.chat_id = :chatId " +
            "AND (cm.chat_member_admin_id = :bot2Id OR cm.chat_member_admin_id is null) " +
            "AND cm.is_read = false ", nativeQuery = true)
    long countUnreadMessagesForChatAndBot2(@Param("chatId") String chatId, @Param("bot2Id") String bot2Id);

    @Query(value = "SELECT c.chat_type AS chatType, COUNT(cm) AS counts " +
            "FROM dating_user_api.chat c " +
            "INNER JOIN dating_user_api.chat_message cm ON c.id = cm.chat_id " +
            "WHERE (cm.chat_member_admin_id = :bot2Id OR cm.chat_member_admin_id IS NULL) " +
            "AND cm.is_read = false " +
            "AND c.id = :chatId " +
            "GROUP BY c.chat_type", nativeQuery = true)
    List<Object[]> countUnreadMessagesByChatTypeAndChatId(@Param("chatId") String chatId,
                                                          @Param("bot2Id") String bot2Id);


    long countByChatId(String chatId);

    long countByChatIdAndChatMemberAdminBot2(String chatId, Bot2 bot2Id);

    List<ChatMessage> findAllByChatIn(List<Chat> chats);
}
