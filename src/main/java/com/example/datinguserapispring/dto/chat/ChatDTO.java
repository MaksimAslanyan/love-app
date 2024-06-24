package com.example.datinguserapispring.dto.chat;


import com.example.datinguserapispring.model.entity.chat.Chat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class ChatDTO {
    private Chat chat;
}
