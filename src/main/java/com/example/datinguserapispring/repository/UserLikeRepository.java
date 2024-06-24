package com.example.datinguserapispring.repository;

import com.example.datinguserapispring.model.entity.user.User;
import com.example.datinguserapispring.model.entity.user.UserLike;
import com.example.datinguserapispring.projection.LikeResponseProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserLikeRepository extends JpaRepository<UserLike, String> {

    @Query(value = "SELECT b.id AS botId, b.name_bot AS nameBot, b.age, b.distance, bos.is_online AS online, ul.is_bot_liked AS isLikedYou " +
            "FROM dating_user_api.user_like ul " +
            "JOIN dating_user_api.bot2 b ON ul.bot2_id = b.id " +
            "LEFT JOIN dating_user_api.bot_online_status bos ON b.id = bos.bot2_id " +
            "WHERE ul.user_info_id = :userId " +
            "AND ul.created_at <= :createdAt " +
            "ORDER BY ul.created_at DESC, b.id ASC", nativeQuery = true)
    Page<LikeResponseProjection> findByUserIdAndCreatedAtBefore(@Param("userId") String userId,
                                                                @Param("createdAt") LocalDateTime createdAt,
                                                                Pageable pageable);

    int countAllByUserIdAndCreatedAtIsAfter(String id, LocalDateTime date);
    boolean existsUserLikeByUserIdAndOpponentId(String userId, String opponentId);
    UserLike findUserLikeByBot2IdAndOpponentId(String userId, String opponentId);
    boolean existsUserLikeByUserIdAndBot2Id(String userId, String bot2Id);
    void deleteAllByUserId(String userId);

    void deleteAllByUserIn(List<User> users);
    void deleteAllByOpponentId(String opponentId);

    @Query(value = "SELECT ul.* FROM dating_user_api.user_like ul " +
            "INNER JOIN dating_user_api.bot2 b ON ul.bot2_id = b.id " +
            "WHERE ul.user_info_id = :userId " +
            "AND b.age BETWEEN :fromAge AND :toAge " +
            "AND ul.is_has_match_chat = false LIMIT 1", nativeQuery = true)
    Optional<UserLike> findUserLikeByUserIdAndBot2AgeRange(@Param("userId") String userId,
                                                           @Param("fromAge") byte fromAge,
                                                           @Param("toAge") byte toAge);


    UserLike findUserLikeByUserIdAndBot2Id(String userId, String bot2id);
}
