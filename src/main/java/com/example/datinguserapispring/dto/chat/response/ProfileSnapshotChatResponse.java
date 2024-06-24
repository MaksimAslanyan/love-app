package com.example.datinguserapispring.dto.chat.response;

import com.example.datinguserapispring.dto.chat.MessageContent;
import com.example.datinguserapispring.dto.photo.response.PhotoDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class ProfileSnapshotChatResponse {
    @JsonProperty("chatId")
    private String chatId;
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("city")
    private String city;
    @JsonProperty("country")
    private String country;
    @JsonProperty("distance")
    private double distance;
    @JsonProperty("isOnline")
    private boolean isOnline;
    @JsonProperty("isBlocked")
    private boolean isBlocked;
    @JsonProperty("isBlockedMe")
    private boolean isBlockedMe;
    @JsonProperty("age")
    private int age;
    @JsonProperty("imageLinks")
    private List<PhotoDTO> imageLinks;
    @JsonProperty("lookingForList")
    private List<String> lookingForList;
    @JsonProperty("isVerified")
    private boolean isVerified;
    @JsonProperty("isLikedMe")
    private boolean isLikedMe;
    @JsonProperty("isPremium")
    private boolean isPremium;
    @JsonProperty("isActive")
    private boolean isActive;
    @JsonProperty("isMatch")
    private boolean isMatch;
    @JsonProperty("name")
    private String name;
    @JsonProperty("isLikedYou")
    private boolean isLikedYou;
    @JsonProperty("messageContent")
    private MessageContent messageContent;
}
