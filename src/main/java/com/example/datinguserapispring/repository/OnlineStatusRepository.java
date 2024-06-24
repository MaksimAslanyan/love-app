package com.example.datinguserapispring.repository;

import com.example.datinguserapispring.model.entity.Admin;
import com.example.datinguserapispring.model.entity.user.OnlineStatus;
import com.example.datinguserapispring.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OnlineStatusRepository extends JpaRepository<OnlineStatus, String> {
    Optional<OnlineStatus> findAllByUser(User user);

    Optional<OnlineStatus> findAllByUserId(String userId);

    Optional<Admin> findAllByAdmin(Admin admin);

    List<OnlineStatus> findByLastOnlineTimeBefore(LocalDateTime dateTime);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM dating_user_api.online_status os WHERE NOT EXISTS (SELECT 1 FROM dating_user_api.user_info u WHERE u.id = os.user_id)", nativeQuery = true)
    void deleteOnlineStatusWithDeletedUsers();

    //    List<OnlineStatus>findAllByUserIdIn(List<String> userIds);
//    @Query(value = "SELECT user_id AS userId, is_online AS isOnline FROM dating_user_api.online_status WHERE user_id IN :userIds", nativeQuery = true)
//    List<OnlineStatusProjection> findAllByUserIdIn(List<String> userIds);

}
