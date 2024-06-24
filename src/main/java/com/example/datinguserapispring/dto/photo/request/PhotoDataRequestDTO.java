package com.example.datinguserapispring.dto.photo.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class PhotoDataRequestDTO {
    private byte isAvatar;
    private String url;
}
