package com.example.datinguserapispring.repository;

import com.example.datinguserapispring.model.entity.user.BlackList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BlackListRepository extends JpaRepository<BlackList, String>, JpaSpecificationExecutor<BlackList> {
    void deleteBlackListByAppleId(String id);
    Page<BlackList> findBlackListsByAdminIdOrAppleId(String adminId, String appleId, Pageable pageable);
    boolean existsByAppleId(String appleId);

}
