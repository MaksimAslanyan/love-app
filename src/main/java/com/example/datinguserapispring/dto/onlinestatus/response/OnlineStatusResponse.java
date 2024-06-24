package com.example.datinguserapispring.dto.onlinestatus.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class OnlineStatusResponse {
    private String userId;
}