package com.example.datinguserapispring.repository;

import com.example.datinguserapispring.model.entity.bot.BotOnlineStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface BotOnlineStatusRepository extends JpaRepository<BotOnlineStatus, String> {

    @Query(value = "SELECT * FROM dating_user_api.bot_online_status " +
            "WHERE last_online_time < ?1 AND is_online = true " +
            "ORDER BY RANDOM() LIMIT 100", nativeQuery = true)
    List<BotOnlineStatus> findRandomInactiveBots(LocalDateTime oneHourAgo);

    @Query(value = "SELECT * FROM dating_user_api.bot_online_status " +
            "WHERE last_online_time < ?1 AND is_online = false " +
            "ORDER BY RANDOM() LIMIT 100", nativeQuery = true)
    List<BotOnlineStatus> findRandomActiveBots(LocalDateTime thirtyMinutesAgo);

    BotOnlineStatus findDistinctFirstByBot2Id(String bot2Id);

    boolean existsByBot2Id(String bot2Id);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM dating_user_api.bot_online_status WHERE bot2_id = :bot2Id", nativeQuery = true)
    void deleteAllByBot2Id(String bot2Id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE dating_user_api.bot_online_status SET is_online = :isOnline WHERE id IN (SELECT id FROM (SELECT id FROM dating_user_api.bot_online_status ORDER BY RANDOM() LIMIT :batchSize) AS sub)", nativeQuery = true)
    void updateRandomBotStatuses(boolean isOnline, long batchSize);


    long countByIsOnlineTrue();

    long countByIsOnlineFalse();
}
