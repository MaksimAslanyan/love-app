package com.example.datinguserapispring.dto.chat.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {
    private String senderId;
    private String receiverId;
    private String targetUserName;
    private String message;
    private String photoId;
    private String chatId;
    private boolean isFirstChatMessage;
}
