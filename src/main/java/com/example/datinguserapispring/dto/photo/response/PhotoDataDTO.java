package com.example.datinguserapispring.dto.photo.response;


import com.example.datinguserapispring.dto.bot.AgeRangeDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class PhotoDataDTO {
    private byte[] image;
    private byte isAvatar;
    private String url;
    private AgeRangeDTO ageRangeDTO;
}
