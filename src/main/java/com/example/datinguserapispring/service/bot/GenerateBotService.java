package com.example.datinguserapispring.service.bot;

import com.example.datinguserapispring.dto.chat.request.StartChatWhenSearchRequest;
import com.example.datinguserapispring.dto.like.request.LikeRequest;
import com.example.datinguserapispring.dto.photo.response.PhotoDataDTO;
import com.example.datinguserapispring.model.entity.bot.Bot2;
import com.example.datinguserapispring.model.entity.user.User;

import java.util.List;

public interface GenerateBotService {

    Bot2 generate(LikeRequest request, String id);
    Bot2 generate(StartChatWhenSearchRequest request, String id);
    Bot2 generate(User user);
    List<Bot2> generateBots(List<User> users);
    void generate();
}
