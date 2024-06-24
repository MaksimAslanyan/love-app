package com.example.datinguserapispring.repository;

import com.example.datinguserapispring.model.entity.dictionary.LookingFor;
import com.example.datinguserapispring.projection.LookingForProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface LookingForRepository extends JpaRepository<LookingFor, String> {
    List<LookingFor> findAllByUserId(String id);
    List<LookingFor> findByLookingForAndUserId(String lookingFor, String userId);
    List<LookingFor> findAllByBot2Id(String id);

    @Query(value = "SELECT lf.bot2_id AS bot2Id, STRING_AGG(lf.looking_for, ',') AS lookingFor " +
            "FROM dating_user_api.looking_for_table lf " +
            "WHERE lf.bot2_id IN :bot2Ids " +
            "GROUP BY lf.bot2_id", nativeQuery = true)
    List<LookingForProjection> findLookingForByBot2IdIn(@Param("bot2Ids") Set<String> bot2Ids);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM dating_user_api.looking_for_table WHERE user_info_id = :userId", nativeQuery = true)
    void deleteAllByUserId(String userId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM dating_user_api.looking_for_table WHERE bot2_id = :bot2Id", nativeQuery = true)
    void deleteAllByBot2Id(String bot2Id);
}
