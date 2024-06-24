package com.example.datinguserapispring.dto.admin;


import com.example.datinguserapispring.model.enums.ActionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class AdminActionFetch {
    private ActionType actionType;
    private String adminId;
    private String appleId;
}
