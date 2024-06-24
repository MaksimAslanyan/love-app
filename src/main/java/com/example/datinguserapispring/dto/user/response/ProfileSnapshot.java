package com.example.datinguserapispring.dto.user.response;

import com.example.datinguserapispring.dto.photo.response.PhotoDTO;
import com.example.datinguserapispring.model.entity.dictionary.LookingFor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class ProfileSnapshot {
    private String id;
    private String name;
    private String gender;
    private int age;
    private boolean isVerified;
    private boolean premium;
    private String country;
    private String city;
    private List<LookingFor> lookingFor;
    private List<PhotoDTO> images;
    private boolean isBlackList;
    private boolean previouslyHadSubscription;
    private boolean existFirebaseToken;
}
