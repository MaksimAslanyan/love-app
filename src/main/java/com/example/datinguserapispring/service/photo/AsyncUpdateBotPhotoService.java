package com.example.datinguserapispring.service.photo;

import com.example.datinguserapispring.model.entity.bot.Bot2;

public interface AsyncUpdateBotPhotoService {

    void updatePhotos(Bot2 bot2, String url);
}
