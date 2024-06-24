package com.example.datinguserapispring.repository;

import com.example.datinguserapispring.model.entity.user.Block;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockRepository extends JpaRepository<Block, String> {
    Block findByBlockingUserIdAndBlockedBotId(String blockingUserId, String blockedBotId);
    boolean existsByBlockingUserIdAndBlockedBotId(String blockingUserId, String blockedBotId);
}
