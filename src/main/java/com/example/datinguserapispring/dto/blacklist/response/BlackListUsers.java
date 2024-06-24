package com.example.datinguserapispring.dto.blacklist.response;


import com.example.datinguserapispring.dto.photo.response.PhotoDTO;
import com.example.datinguserapispring.model.entity.dictionary.LookingFor;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class BlackListUsers {
    private String id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private String adminName;
    private String name;
    private String gender;
    private String cityName;
    private String country;
    private List<LookingFor> lookingFor;
    private String appleId;
    private List<PhotoDTO> images;
}
