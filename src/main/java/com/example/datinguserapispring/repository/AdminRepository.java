package com.example.datinguserapispring.repository;

import com.example.datinguserapispring.model.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, String> {

    Optional<Admin> findByLoginAndPassword(String login, String password);

    Optional<Admin> findByLogin(String login);

    boolean existsByLoginOrNickName(String login, String nickName);

    @Query(value = "SELECT * FROM dating_user_api.admin_db WHERE role = 512 " +
            "ORDER BY RANDOM() LIMIT :limit",
            nativeQuery = true)
    List<Admin> findRandomAdminWithRole(@Param("limit") int limit);

    @Query(value = "SELECT * FROM dating_user_api.admin_db WHERE role = 1024 " +
            "ORDER BY RANDOM() LIMIT :limit",
            nativeQuery = true)
    List<Admin> findRandomAdminWithRoleOwner(@Param("limit") int limit);

    @Query(value = "SELECT * FROM dating_user_api.admin_db a WHERE a.role = :role AND a.is_deleted = false AND a.id <> :excludedAdminId ",nativeQuery = true)
    List<Admin> findAllByRoleAndIsDeletedIsFalseAndNotExcluded(@Param("role") int role, @Param("excludedAdminId") String excludedAdminId);
    List<Admin> findAllByIsDeletedIsFalse();
}
