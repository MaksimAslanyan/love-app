package com.example.datinguserapispring.repository;

import com.example.datinguserapispring.model.entity.user.PremiumPeriod;
import com.example.datinguserapispring.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PremiumPeriodRepository extends JpaRepository<PremiumPeriod, String> {

    @Query(value = "SELECT COUNT(pp) > 0 " +
            "FROM dating_user_api.premium_period pp " +
            "WHERE pp.user_info_id = :user AND pp.active = false AND pp.until_date > :currentDateTime", nativeQuery = true)
    boolean hasActivePremiumPeriods(@Param("user") User user, @Param("currentDateTime") LocalDateTime currentDateTime);
    List<PremiumPeriod> findAllByUserId(String userId);

    PremiumPeriod findFirstByUserOrderByFromDateDesc(User user);

    List<PremiumPeriod> findByUntilDateBeforeAndActiveTrue(LocalDateTime currentDateTime);

    boolean existsAllByUserIdAndActive(String userId, boolean active);

    void deleteAllByUserId(String id);

    boolean existsByUserId(String userId);
}
