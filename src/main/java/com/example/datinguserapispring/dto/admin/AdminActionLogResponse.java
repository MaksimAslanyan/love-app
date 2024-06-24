package com.example.datinguserapispring.dto.admin;


import com.example.datinguserapispring.model.enums.ActionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class AdminActionLogResponse {
    private String userId;
    private String appleId;
    private String imageId;
    private String adminName;
    private String adminRole;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private ActionType actionType;

    public AdminActionLogResponse(String userId) {
        this.userId = userId;
    }
}
