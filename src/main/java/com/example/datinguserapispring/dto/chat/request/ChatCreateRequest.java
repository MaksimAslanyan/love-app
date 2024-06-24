package com.example.datinguserapispring.dto.chat.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatCreateRequest {
    private String firstUserId;
    private String secondUserId;
}
