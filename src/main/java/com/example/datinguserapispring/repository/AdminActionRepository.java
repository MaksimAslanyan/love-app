package com.example.datinguserapispring.repository;

import com.example.datinguserapispring.model.entity.user.AdminAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AdminActionRepository extends JpaRepository<AdminAction, String>, JpaSpecificationExecutor<AdminAction> {}
