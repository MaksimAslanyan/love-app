package com.example.datinguserapispring.repository;

import com.example.datinguserapispring.model.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, String> {

    List<Location> findLocationByUserId(String userId);

    Optional<Location> findFirstByUserIdOrderByCreatedAtDesc(String userId);

    void deleteAllByUserId(String userId);
}
