package com.example.datinguserapispring.dto.photo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class WaitingApprovalPhotoDTO {
    private String id;
}
