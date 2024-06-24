package com.example.datinguserapispring.dto.chat;

import lombok.Data;

import java.util.List;

@Data
public class SeenMessage {
    private List<String> messageIds;
}
