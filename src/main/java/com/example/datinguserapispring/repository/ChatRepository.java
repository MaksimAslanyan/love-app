package com.example.datinguserapispring.repository;

import com.example.datinguserapispring.model.entity.chat.Chat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.example.datinguserapispring.projection.AdminChatProjection;
import com.example.datinguserapispring.projection.ChatProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, String>, JpaSpecificationExecutor<Chat> {

    Optional<Chat> findById(String chatId);

    void deleteAllByIdIn(List<String> ids);

    @Query(value = "SELECT " +
            "c.id AS chatId, " +
            "c.is_match AS isMatch, " +
            "b.id AS bot2Id, " +
            "b.age, " +
            "b.name_bot AS name, " +
            "b.distance AS distance, " +
            "bos.is_online AS isOnline, " +
            "cm_latest.chatMessageId AS chatMessageId, " +
            "cm_latest.lastMessageDate AS lastMessageDate, " +
            "cm_latest.lastMessage AS lastMessage, " +
            "cm_latest.isRead AS isRead, " +
            "cm_latest.chatMember AS chatMember, " +
            "cm_latest.chatMmemberAdmin AS chatMemberAdmin, " +
            "cm_latest.photoId AS photoId " +
            "FROM " +
            "dating_user_api.chat_member cm " +
            "INNER JOIN dating_user_api.chat c ON cm.chat_id = c.id " +
            "INNER JOIN dating_user_api.chat_member_admin cma ON c.id = cma.chat_id " +
            "INNER JOIN dating_user_api.bot2 b ON cma.bot2_id = b.id " +
            "LEFT JOIN dating_user_api.bot_online_status bos ON b.id = bos.bot2_id " +
            "LEFT JOIN ( " +
            "SELECT " +
            "cm_sub.chat_id, " +
            "cm_sub.id AS chatMessageId, " +
            "cm_sub.created_date AS lastMessageDate, " +
            "cm_sub.message AS lastMessage, " +
            "cm_sub.is_read AS isRead, " +
            "cm_sub.chat_member_id AS chatMember, " +
            "cm_sub.chat_member_admin_id AS chatMmemberAdmin, " +
            "cm_sub.photo_id AS photoId, " +
            "ROW_NUMBER() OVER (PARTITION BY cm_sub.chat_id ORDER BY cm_sub.created_date DESC) AS row_num " +
            "FROM ( " +
            "SELECT " +
            "cm_inner.chat_id, " +
            "cm_inner.id, " +
            "cm_inner.created_date, " +
            "cm_inner.message, " +
            "cm_inner.is_read, " +
            "cm_inner.chat_member_id, " +
            "cm_inner.chat_member_admin_id, " +
            "cm_inner.photo_id, " +
            "ROW_NUMBER() OVER (PARTITION BY cm_inner.chat_id ORDER BY cm_inner.created_date DESC) AS row_num " +
            "FROM " +
            "dating_user_api.chat_message cm_inner " +
            ") cm_sub " +
            "WHERE cm_sub.row_num = 1 " +
            ") cm_latest ON c.id = cm_latest.chat_id " +
            "WHERE " +
            "c.created_date <= :now AND cm.user_id = :userId AND (" +
            "(c.is_match = FALSE AND EXISTS (SELECT 1 FROM dating_user_api.chat_message cm_check WHERE cm_check.chat_id = c.id)) " +
            "OR " +
            "(c.is_match = TRUE AND NOT EXISTS (SELECT 1 FROM dating_user_api.chat_message cm_check WHERE cm_check.chat_id = c.id))" +
            ") " +
            "ORDER BY c.last_activity DESC", nativeQuery = true)
    List<ChatProjection> findChatsByUserId(
            @Param("now") LocalDateTime now,
            @Param("userId") String userId,
            Pageable pageable
    );

    @Query(value = "SELECT " +
            "chat.id AS chatId, " +
            "chat.last_activity AS lastActivity, " +
            "chat.chat_type AS chatType, " +
            "user_info1.id AS userId, " +
            "user_info1.gender AS gender, " +
            "user_info1.name AS name, " +
            "user_info1.is_premium AS isPremium, " +
            "user_info1.is_black_list AS isBlackList, " +
            "address.city AS city, " +
            "address.country AS country, " +
            "online_status.is_online AS isOnline, " +
            "bot2.id AS bot2Id, " +
            "bot2.age, " +
            "bot2.name_bot AS nameBot, " +
            "bot1.id AS bot1Id, " +
            "bot1.gender AS botGender, " +
            "bot1.race AS race, " +
            "bot1.min_age AS minAge, " +
            "bot1.max_age AS maxAge, " +
            "cm_latest.chatMessageId AS chatMessageId, " +
            "cm_latest.lastMessageDate AS lastMessageDate, " +
            "cm_latest.lastMessage AS lastMessage, " +
            "cm_latest.isRead AS isRead, " +
            "cm_latest.chatMember AS chatMemberId, " +
            "cm_latest.chatMmemberAdmin AS chatMemberAdminId, " +
            "cm_latest.photoId AS photoId " +
            "FROM " +
            "dating_user_api.chat_member_admin cm " +
            "INNER JOIN dating_user_api.bot2 bot2 ON cm.bot2_id = bot2.id " +
            "INNER JOIN dating_user_api.chat chat ON cm.chat_id = chat.id " +
            "INNER JOIN dating_user_api.bot1 bot1 ON bot2.bot1_id = bot1.id " +
            "INNER JOIN dating_user_api.chat_member cmbr ON chat.id = cmbr.chat_id " +
            "INNER JOIN dating_user_api.user_info user_info1 ON cmbr.user_id = user_info1.id " +
            "LEFT JOIN dating_user_api.address address ON user_info1.id = address.user_info_id " +
            "LEFT JOIN dating_user_api.online_status online_status ON user_info1.id = online_status.user_id " +
            "LEFT JOIN ( " +
            "SELECT " +
            "cm_sub.chat_id, " +
            "cm_sub.id AS chatMessageId, " +
            "cm_sub.created_date AS lastMessageDate, " +
            "cm_sub.message AS lastMessage, " +
            "cm_sub.is_read AS isRead, " +
            "cm_sub.chat_member_id AS chatMember, " +
            "cm_sub.chat_member_admin_id AS chatMmemberAdmin, " +
            "cm_sub.photo_id AS photoId, " +
            "ROW_NUMBER() OVER (PARTITION BY cm_sub.chat_id ORDER BY cm_sub.created_date DESC) AS row_num " +
            "FROM ( " +
            "SELECT " +
            "cm_inner.chat_id, " +
            "cm_inner.id, " +
            "cm_inner.created_date, " +
            "cm_inner.message, " +
            "cm_inner.is_read, " +
            "cm_inner.chat_member_id, " +
            "cm_inner.chat_member_admin_id, " +
            "cm_inner.photo_id, " +
            "ROW_NUMBER() OVER (PARTITION BY cm_inner.chat_id ORDER BY cm_inner.created_date DESC) AS row_num " +
            "FROM " +
            "dating_user_api.chat_message cm_inner " +
            ") cm_sub " +
            "WHERE cm_sub.row_num = 1 " +
            ") cm_latest ON chat.id = cm_latest.chat_id " +
            "WHERE " +
            "bot2.admin_db_id = :adminId " +
            "AND (:category = '' OR chat.chat_type = :category) " +
            "AND chat.chat_type != 'NULL' " +
            "ORDER BY chat.last_activity DESC", nativeQuery = true)
    List<AdminChatProjection> findChatsByAdminIdAndChatType(@Param("adminId") String adminId, @Param("category") String category, Pageable pageable);


    @Query(value = "SELECT " +
            "chat.id AS chatId, " +
            "chat.last_activity AS lastActivity, " +
            "chat.chat_type AS chatType, " +
            "user_info1.id AS userId, " +
            "user_info1.gender AS gender, " +
            "user_info1.name AS name, " +
            "user_info1.is_premium AS isPremium, " +
            "user_info1.is_black_list AS isBlackList, " +
            "address.city AS city, " +
            "address.country AS country, " +
            "online_status.is_online AS isOnline, " +
            "bot2.id AS bot2Id, " +
            "bot2.age, " +
            "bot2.name_bot AS nameBot, " +
            "bot1.id AS bot1Id, " +
            "bot1.gender AS botGender, " +
            "bot1.race AS race, " +
            "bot1.min_age AS minAge, " +
            "bot1.max_age AS maxAge, " +
            "cm_latest.chatMessageId AS chatMessageId, " +
            "cm_latest.lastMessageDate AS lastMessageDate, " +
            "cm_latest.lastMessage AS lastMessage, " +
            "cm_latest.isRead AS isRead, " +
            "cm_latest.chatMember AS chatMemberId, " +
            "cm_latest.chatMmemberAdmin AS chatMemberAdminId, " +
            "cm_latest.photoId AS photoId " +
            "FROM " +
            "dating_user_api.chat_member_admin cm " +
            "INNER JOIN dating_user_api.bot2 bot2 ON cm.bot2_id = bot2.id " +
            "INNER JOIN dating_user_api.chat chat ON cm.chat_id = chat.id " +
            "INNER JOIN dating_user_api.bot1 bot1 ON bot2.bot1_id = bot1.id " +
            "INNER JOIN dating_user_api.chat_member cmbr ON chat.id = cmbr.chat_id " +
            "INNER JOIN dating_user_api.user_info user_info1 ON cmbr.user_id = user_info1.id " +
            "LEFT JOIN dating_user_api.address address ON user_info1.id = address.user_info_id " +
            "LEFT JOIN dating_user_api.online_status online_status ON user_info1.id = online_status.user_id " +
            "LEFT JOIN ( " +
            "SELECT " +
            "cm_sub.chat_id, " +
            "cm_sub.id AS chatMessageId, " +
            "cm_sub.created_date AS lastMessageDate, " +
            "cm_sub.message AS lastMessage, " +
            "cm_sub.is_read AS isRead, " +
            "cm_sub.chat_member_id AS chatMember, " +
            "cm_sub.chat_member_admin_id AS chatMmemberAdmin, " +
            "cm_sub.photo_id AS photoId, " +
            "ROW_NUMBER() OVER (PARTITION BY cm_sub.chat_id ORDER BY cm_sub.created_date DESC) AS row_num " +
            "FROM ( " +
            "SELECT " +
            "cm_inner.chat_id, " +
            "cm_inner.id, " +
            "cm_inner.created_date, " +
            "cm_inner.message, " +
            "cm_inner.is_read, " +
            "cm_inner.chat_member_id, " +
            "cm_inner.chat_member_admin_id, " +
            "cm_inner.photo_id, " +
            "ROW_NUMBER() OVER (PARTITION BY cm_inner.chat_id ORDER BY cm_inner.created_date DESC) AS row_num " +
            "FROM " +
            "dating_user_api.chat_message cm_inner " +
            ") cm_sub " +
            "WHERE cm_sub.row_num = 1 " +
            ") cm_latest ON chat.id = cm_latest.chat_id " +
            "WHERE " +
            "bot2.admin_db_id = :adminId " +
            "AND chat.chat_type = :category " +
            "ORDER BY chat.last_activity DESC", nativeQuery = true)
    List<AdminChatProjection> findChatsByAdminIdAndChatTypeIsNull(@Param("adminId") String adminId, @Param("category") String category, Pageable pageable);

    @Query(value = "SELECT " +
            "chat.id AS chatId, " +
            "chat.last_activity AS lastActivity, " +
            "chat.chat_type AS chatType, " +
            "user_info1.id AS userId, " +
            "user_info1.gender AS gender, " +
            "user_info1.name AS name, " +
            "user_info1.is_premium AS isPremium, " +
            "user_info1.is_black_list AS isBlackList, " +
            "address.city AS city, " +
            "address.country AS country, " +
            "online_status.is_online AS isOnline, " +
            "bot2.id AS bot2Id, " +
            "bot2.age, " +
            "bot2.name_bot AS nameBot, " +
            "bot1.id AS bot1Id, " +
            "bot1.gender AS botGender, " +
            "bot1.race AS race, " +
            "bot1.min_age AS minAge, " +
            "bot1.max_age AS maxAge, " +
            "cm_latest.chatMessageId AS chatMessageId, " +
            "cm_latest.lastMessageDate AS lastMessageDate, " +
            "cm_latest.lastMessage AS lastMessage, " +
            "cm_latest.isRead AS isRead, " +
            "cm_latest.chatMember AS chatMemberId, " +
            "cm_latest.chatMmemberAdmin AS chatMemberAdminId, " +
            "cm_latest.photoId AS photoId " +
            "FROM " +
            "dating_user_api.chat_member_admin cm " +
            "INNER JOIN dating_user_api.bot2 bot2 ON cm.bot2_id = bot2.id " +
            "INNER JOIN dating_user_api.chat chat ON cm.chat_id = chat.id " +
            "INNER JOIN dating_user_api.bot1 bot1 ON bot2.bot1_id = bot1.id " +
            "INNER JOIN dating_user_api.chat_member cmbr ON chat.id = cmbr.chat_id " +
            "INNER JOIN dating_user_api.user_info user_info1 ON cmbr.user_id = user_info1.id " +
            "LEFT JOIN dating_user_api.address address ON user_info1.id = address.user_info_id " +
            "LEFT JOIN dating_user_api.online_status online_status ON user_info1.id = online_status.user_id " +
            "LEFT JOIN ( " +
            "SELECT " +
            "cm_sub.chat_id, " +
            "cm_sub.id AS chatMessageId, " +
            "cm_sub.created_date AS lastMessageDate, " +
            "cm_sub.message AS lastMessage, " +
            "cm_sub.is_read AS isRead, " +
            "cm_sub.chat_member_id AS chatMember, " +
            "cm_sub.chat_member_admin_id AS chatMmemberAdmin, " +
            "cm_sub.photo_id AS photoId, " +
            "ROW_NUMBER() OVER (PARTITION BY cm_sub.chat_id ORDER BY cm_sub.created_date DESC) AS row_num " +
            "FROM ( " +
            "SELECT " +
            "cm_inner.chat_id, " +
            "cm_inner.id, " +
            "cm_inner.created_date, " +
            "cm_inner.message, " +
            "cm_inner.is_read, " +
            "cm_inner.chat_member_id, " +
            "cm_inner.chat_member_admin_id, " +
            "cm_inner.photo_id, " +
            "ROW_NUMBER() OVER (PARTITION BY cm_inner.chat_id ORDER BY cm_inner.created_date DESC) AS row_num " +
            "FROM " +
            "dating_user_api.chat_message cm_inner " +
            ") cm_sub " +
            "WHERE cm_sub.row_num = 1 " +
            ") cm_latest ON chat.id = cm_latest.chat_id " +
            "WHERE " +
            "bot2.admin_db_id = :adminId " +
            "AND user_info1.name = :name " +
            "ORDER BY chat.last_activity DESC", nativeQuery = true)
    List<AdminChatProjection> findChatsByAdminIdAndUserName(@Param("adminId") String adminId, @Param("name") String name, Pageable pageable);
}
