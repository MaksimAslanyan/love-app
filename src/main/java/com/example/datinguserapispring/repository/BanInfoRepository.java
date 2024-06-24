package com.example.datinguserapispring.repository;

import com.example.datinguserapispring.model.entity.user.BanInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BanInfoRepository extends JpaRepository<BanInfo, String> {

    Optional<BanInfo> findFirstByUserId(String id);
    void deleteBanInfoByUserId(String id);
}
