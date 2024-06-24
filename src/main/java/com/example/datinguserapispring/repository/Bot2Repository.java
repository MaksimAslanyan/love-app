package com.example.datinguserapispring.repository;

import com.example.datinguserapispring.model.entity.bot.Bot2;
import com.example.datinguserapispring.model.enums.ParentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface Bot2Repository extends JpaRepository<Bot2, String>, JpaSpecificationExecutor<Bot2> {

    Page<Bot2> findAllByBot1IdAndParentType(String id, ParentType parentType, Pageable pageable);

    Page<Bot2> findByIdAndBot1IdAndParentTypeContainingIgnoreCase(String id, String bot1Id, String parentType, Pageable pageable);
    List<Bot2> findAllByAdminId(String adminId);

    List<Bot2> findAllByUserId(String userId);
    Page<Bot2> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM dating_user_api.bot2 WHERE parent_type = 'ADMIN' ORDER BY RANDOM() LIMIT :limit",
            nativeQuery = true)
    List<Bot2> findRandomBots2(@Param("limit") int limit);

    Page<Bot2> findAllById(String id, Pageable pageable);

    @Query(value = "SELECT * FROM dating_user_api.bot2 WHERE " +
            "user_info_id = :userId " +
            "AND age " +
            "BETWEEN :minAge " +
            "AND :maxAge " +
            "ORDER BY RANDOM() LIMIT :limit", nativeQuery = true)
    Bot2 findBot2ByUserIdAndAgeRange(@Param("userId") String userId,
                                     @Param("minAge") byte minAge,
                                     @Param("maxAge") byte maxAge,
                                     @Param("limit") int limit);

    long countBot2ByUserId(String userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM dating_user_api.bot2 WHERE id = :bot2Id", nativeQuery = true)
    void deleteBot2ById(String bot2Id);
}
