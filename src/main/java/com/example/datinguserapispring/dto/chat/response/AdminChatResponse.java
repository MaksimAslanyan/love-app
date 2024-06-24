package com.example.datinguserapispring.dto.chat.response;

import com.example.datinguserapispring.dto.bot.response.BotProfileSnapshotForAdmin;
import com.example.datinguserapispring.dto.chat.MessageContent;
import com.example.datinguserapispring.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class AdminChatResponse {
    private String chatId;
    private MessageContent messageContent;
    private UserDTO user;
    private BotProfileSnapshotForAdmin bot2;
    private long unReadMessagesCount;
}
