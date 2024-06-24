package com.example.datinguserapispring.service.lookingFor;

import com.example.datinguserapispring.model.entity.user.User;

import java.util.List;

public interface LookingForService {

    List<String> getUserLookingFor(String id);
    List<String> getBotLookingFor(String id);
    List<String> getUserLookingFor(User user);
}
