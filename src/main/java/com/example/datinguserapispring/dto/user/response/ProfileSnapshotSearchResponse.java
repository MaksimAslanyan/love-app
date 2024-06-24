package com.example.datinguserapispring.dto.user.response;


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
public class ProfileSnapshotSearchResponse {
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
    private List<PhotoDTO> imageLinks;
    @JsonProperty("isVerified")
    private boolean isVerified;
    @JsonProperty("isLikedMe")
    private boolean isLikedMe;
    @JsonProperty("isBlockedMe")
    private boolean isBlockedMe;
    @JsonProperty("isBlocked")
    private boolean isBlocked;
    @JsonProperty("isPremium")
    private boolean isPremium;
    @JsonProperty("lookingForList")
    private List<String> lookingForList;
    @JsonProperty("name")
    private String name;
    @JsonProperty("isLikedYou")
    private boolean isLikedYou;
}
