package com.example.datinguserapispring.dto.user;


import com.example.datinguserapispring.dto.photo.response.PhotoDTO;
import com.example.datinguserapispring.model.enums.Gender;
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
public class UserDTO {
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("city")
    private String city;
    @JsonProperty("country")
    private String country;
    @JsonProperty("isOnline")
    private boolean isOnline;
    @JsonProperty("images")
    private List<PhotoDTO> images;
    @JsonProperty("name")
    private String name;
    @JsonProperty("gender")
    private Gender gender;
    @JsonProperty("isPremium")
    private boolean isPremium;
    @JsonProperty("isBlackList")
    private boolean isBlackList;
}
