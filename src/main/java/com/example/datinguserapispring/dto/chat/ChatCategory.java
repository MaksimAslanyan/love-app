package com.example.datinguserapispring.dto.chat;


import com.example.datinguserapispring.model.enums.ChatType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChatCategory {
    private ChatType chatType;
}
