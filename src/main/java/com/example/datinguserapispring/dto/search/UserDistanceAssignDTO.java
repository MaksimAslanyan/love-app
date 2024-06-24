package com.example.datinguserapispring.dto.search;


import com.example.datinguserapispring.dto.photo.response.PhotoDTO;
import com.example.datinguserapispring.model.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class UserDistanceAssignDTO {
    private User user;
    private String country;
    private String city;
    private List<PhotoDTO> images;
    private double opponentDistance;
    private boolean likedMe;
}
