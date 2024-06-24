package com.example.datinguserapispring.dto.like.request;

import com.example.datinguserapispring.dto.photo.request.PhotoDataRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class LikeRequest {
    private String id;
    private String name;
    private List<PhotoDataRequestDTO> photoDataDTOS;
    private List<String> lookingForList;
    private boolean isLike;
    private double distance;
    private byte age;
}
