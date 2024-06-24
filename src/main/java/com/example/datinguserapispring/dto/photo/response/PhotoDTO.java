package com.example.datinguserapispring.dto.photo.response;


import com.example.datinguserapispring.model.enums.PhotoState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class PhotoDTO {
    private String id;
    private byte isAvatar;
    private String url;
    private String createdAt;
    private PhotoState photoState;

    public PhotoDTO(String id, String url) {
        this.id = id;
        this.url = url;
    }
}
