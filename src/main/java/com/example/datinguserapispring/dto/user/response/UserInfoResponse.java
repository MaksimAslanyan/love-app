package com.example.datinguserapispring.dto.user.response;


import com.example.datinguserapispring.dto.photo.response.PhotoDTO;
import com.example.datinguserapispring.model.entity.dictionary.LookingFor;
import com.example.datinguserapispring.model.enums.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserInfoResponse {
    @JsonProperty("id")
    private String id;
    @JsonProperty("isBlocked")
    private Boolean isBlocked;
    @JsonProperty("createdAt") @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;
    @JsonProperty("appleId")
    private String appleId;
    @JsonProperty("lastActivity")
    private LocalDateTime lastActivity;
    @JsonProperty("gender")
    private Gender gender;
    @JsonProperty("dob")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;
    @JsonProperty("name")
    private String name;
    @JsonProperty("language")
    private String language;
    @JsonProperty("cityName")
    private String cityName;
    @JsonProperty("targetGender")
    private String targetGender;
    @JsonProperty("lookingFor")
    private List<LookingFor> lookingFor;
    @JsonProperty("images")
    private List<PhotoDTO> images;
    @JsonProperty("age")
    private int age;
    @JsonProperty("roles")
    private String roles;
    @JsonProperty("country")
    private String country;



    @Override
    public String toString() {
        return "UserInfoResponse{" +
                "id='" + id + '\'' +
                ", isBlocked=" + isBlocked +
                ", createdAt=" + createdAt +
                ", appleId='" + appleId + '\'' +
                ", lastActivity=" + lastActivity +
                ", gender=" + gender +
                ", dob=" + dob +
                ", name='" + name + '\'' +
                ", language='" + language + '\'' +
                ", cityName='" + cityName + '\'' +
                ", targetGender='" + targetGender + '\'' +
                ", lookingFor='" + lookingFor + '\'' +
                ", images=" + images +
                ", age=" + age +
                ", roles='" + roles + '\'' +
                '}';
    }
}
