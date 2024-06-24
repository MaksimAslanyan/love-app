package com.example.datinguserapispring.repository;

import com.example.datinguserapispring.model.entity.bot.Bot1;
import com.example.datinguserapispring.model.enums.ParentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface Bot1Repository extends JpaRepository<Bot1, String> {
    @Query(value = "SELECT * FROM dating_user_api.bot1 WHERE gender = :genderParam " +
            "AND min_age <= :maxAgeParam " +
            "AND max_age >= :minAgeParam " +
            "AND race = :raceParam " +
            "AND parent_type = :parentType " +
            "ORDER BY RANDOM() LIMIT :limit ",
            nativeQuery = true)
    List<Bot1> findRandomBot1ByGenderAndRace(@Param("limit") int limit,
                                             @Param("genderParam") String gender,
                                             @Param("minAgeParam") byte minAge,
                                             @Param("maxAgeParam") byte maxAge,
                                             @Param("raceParam") String race,
                                             @Param("parentType") String parentType);

    Page<Bot1> findAllByParentTypeAndRace(ParentType parentType, String parenType, Pageable pageable);

    long count();

    List<Bot1> findAllByAdminIdAndParentType(String id, ParentType parentType);

    Optional<List<Bot1>> findAllByAdminId(String bot1Id);

    void deleteAllByAdminId(String id);
}
