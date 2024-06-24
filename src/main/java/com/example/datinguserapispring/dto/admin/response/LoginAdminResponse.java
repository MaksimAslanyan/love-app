package com.example.datinguserapispring.dto.admin.response;

import com.example.datinguserapispring.dto.photo.response.PhotoDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class LoginAdminResponse {
    private String adminId;
    private String token;
    private String name;
    private String tg;
    private List<PhotoDTO> images;
    private int role;
}
