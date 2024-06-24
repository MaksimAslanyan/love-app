package com.example.datinguserapispring.dto.like.response;

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
public class UserLikeResponse {
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("age")
    private int age;
    @JsonProperty("isVerified")
    private boolean isVerified;
    @JsonProperty("distance")
    private double distance;
    @JsonProperty("city")
    private String city;
    @JsonProperty("country")
    private String country;
    @JsonProperty("isOnline")
    private boolean isOnline;
    @JsonProperty("imageLinks")
    private List<PhotoDTO> imageLinks;
    @JsonProperty("lookingForList")
    private List<String> lookingForList;
    @JsonProperty("isLikedMe")
    private boolean isLikedMe;
    @JsonProperty("isActive")
    private boolean isActive;
    @JsonProperty("isBlocked")
    private boolean isBlocked;
    @JsonProperty("isPremium")
    private boolean isPremium;
    @JsonProperty("isBlockedMe")
    private boolean isBlockedMe;
    @JsonProperty("isLikedYou")
    private boolean isLikedYou;
}
