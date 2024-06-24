package com.example.datinguserapispring.repository;

import com.example.datinguserapispring.model.entity.user.Address;
import com.example.datinguserapispring.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, String> {
    List<Address> findAllByUser(User user);
    Address getFirstByUserId(String userId);
    Address findTop1ByUserId(String userId);
    void deleteAllByUserId(String userId);

    //TEST sonar
}
