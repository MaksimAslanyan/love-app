package com.example.datinguserapispring.projection;

import com.example.datinguserapispring.model.entity.Photo;

import java.util.List;

public interface LikeResponseProjection {

    String getBotId();
    String getNameBot();
    Byte getAge();
    Double getDistance();
    Boolean getOnline();
    List<Photo> getPhotos();
    Boolean getIsLikedYou();

}
