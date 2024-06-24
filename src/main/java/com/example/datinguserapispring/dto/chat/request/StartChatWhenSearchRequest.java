package com.example.datinguserapispring.dto.chat.request;

import com.example.datinguserapispring.dto.photo.request.PhotoDataRequestDTO;
import lombok.Data;

import java.util.List;

@Data
public class StartChatWhenSearchRequest {
    private String firstUserId;
    private String secondUserId;
    private String name;
    private List<PhotoDataRequestDTO> photoDataDTOS;
    private List<String> lookingForList;
    private boolean isLike;
    private double distance;
    private byte age;
}
