package com.example.datinguserapispring.dto.admin;


import com.example.datinguserapispring.model.entity.Admin;
import com.example.datinguserapispring.model.entity.user.User;
import com.example.datinguserapispring.model.enums.ActionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class AdminActionLogDTO {
    private User user;
    private Admin admin;
    private LocalDateTime createdAt;
    private ActionType actionType;
}
