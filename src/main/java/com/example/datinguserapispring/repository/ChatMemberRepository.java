package com.example.datinguserapispring.repository;

import com.example.datinguserapispring.model.entity.chat.ChatMember;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMemberRepository extends JpaRepository<ChatMember, String>,
    JpaSpecificationExecutor<ChatMember> {

  Optional<ChatMember> findByChatIdAndUserId(String chatId, String userId);

  Optional<List<ChatMember>> findByUserId(String userId);

  List<ChatMember> findChatMemberByUserId(String userId);

  @Query(value = "SELECT chat_id FROM dating_user_api.chat_member WHERE user_id = :userId",
      nativeQuery = true)
  List<String> findAllChatIdsByUserId(String userId);

}
