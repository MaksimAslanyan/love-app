package com.example.datinguserapispring.repository;

import com.example.datinguserapispring.model.entity.user.User;
import com.example.datinguserapispring.model.enums.Gender;
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
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    Optional<User> findFirstByAppleId(String appleId);

    User findUserByToken(String token);

    Long countUserByGenderAndIsBlackListIsFalse(Gender gender);

    Optional<User> findById(String id);

    @Query("SELECT u FROM User u WHERE u.isActive = true AND u.token IS NOT NULL AND u.isBlackList = false AND u.targetGender IS NOT NULL AND u.targetGender IN ('MALE', 'FEMALE') ORDER BY u.createdAt ASC ")
    Page<User> findAllActiveUsersWithTokenAndNotBlackListedAndTargetGenderOrNotChosenAsc(Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.isActive = true AND u.token IS NOT NULL AND u.isBlackList = false AND u.targetGender IS NOT NULL AND u.targetGender IN ('MALE', 'FEMALE') ORDER BY u.createdAt DESC ")
    Page<User> findAllActiveUsersWithTokenAndNotBlackListedAndTargetGenderOrNotChosenDesc(Pageable pageable);


    Page<User> findAllByIsActiveFalse(Pageable pageable);


    @Query(value = "SELECT * FROM dating_user_api.user_info ORDER BY RANDOM() LIMIT :limit",
            nativeQuery = true)
    List<User> findRandomUsers(@Param("limit") int limit);

    @Modifying
    @Query(value = "UPDATE dating_user_api.user_info SET token = ?1 WHERE id = ?2", nativeQuery = true)
    void updateUserToken(String token, String userId);

    @Modifying
    @Query(value = "UPDATE dating_user_api.user_info SET token = ?1 WHERE token = ?2", nativeQuery = true)
    void updateUserTokenByToken(String newToken, String oldToken);

    @Transactional
    @Modifying
    @Query(value = "UPDATE dating_user_api.user_info SET like_count = 0", nativeQuery = true)
    void resetLikeCounts();

    @Query(value = "SELECT EXISTS (SELECT 1 FROM dating_user_api.bot2 b " +
            "JOIN dating_user_api.photo p ON b.id = p.bot2_id " +
            "WHERE b.user_info_id = :userId AND p.url = :url)",
            nativeQuery = true)
    boolean existsByUserIdAndUrl(String userId, String url);

    @Query("SELECT COUNT(u) FROM User u WHERE u.isActive = true AND u.token IS NOT NULL AND u.isBlackList = false AND u.targetGender IS NOT NULL AND u.targetGender IN ('MALE', 'FEMALE')")
    int countAllActiveUsersWithTokenAndNotBlackListedAndTargetGenderOrNotChosen();
}
