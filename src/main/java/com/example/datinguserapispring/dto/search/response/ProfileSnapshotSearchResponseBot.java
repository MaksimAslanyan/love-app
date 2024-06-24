package com.example.datinguserapispring.dto.search.response;

import com.example.datinguserapispring.dto.photo.response.PhotoDataDTO;
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
public class ProfileSnapshotSearchResponseBot {
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
    @JsonProperty("age")
    private int age;
    @JsonProperty("imageLinks")
    private List<PhotoDataDTO> imageLinks;
    @JsonProperty("lookingForList")
    private List<String> lookingForList;
    @JsonProperty("isVerified")
    private boolean isVerified;
    @JsonProperty("isLikedMe")
    private boolean isLikedMe;
    @JsonProperty("isPremium")
    private boolean isPremium;
    @JsonProperty("isBlockedMe")
    private boolean isBlockedMe;
    @JsonProperty("isBlocked")
    private boolean isBlocked;
    @JsonProperty("isActive")
    private boolean isActive;
    @JsonProperty("name")
    private String name;
    @JsonProperty("isLikedYou")
    private boolean isLikedYou;
}
