package com.example.datinguserapispring.dto.photo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class DeletePhotoResponse {
    @JsonProperty("deletedId")
    private String deletedId;
    @JsonProperty("isDelete")
    private boolean isDelete;
}
