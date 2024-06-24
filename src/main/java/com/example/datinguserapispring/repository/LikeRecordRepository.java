package com.example.datinguserapispring.repository;

import com.example.datinguserapispring.model.entity.user.LikeRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface LikeRecordRepository extends JpaRepository<LikeRecord, String> {
    int countAllByUserIdAndLikedAtIsAfter(String id, LocalDateTime date);
}
