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
public class ListAdminItemResponse {
    private String id;
    private String login;
    private String name;
    private String tg;
    private Integer authority;
    private List<PhotoDTO> images;
    private List<String> bot1Ids;

}
