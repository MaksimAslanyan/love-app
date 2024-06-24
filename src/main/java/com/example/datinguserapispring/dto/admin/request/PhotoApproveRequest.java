package com.example.datinguserapispring.dto.admin.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class PhotoApproveRequest {
    @JsonProperty("imageId")
    private String imageId;
    @JsonProperty("isApprove")
    private boolean isApprove;
}
