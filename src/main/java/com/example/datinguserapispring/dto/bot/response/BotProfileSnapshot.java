package com.example.datinguserapispring.dto.bot.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class BotProfileSnapshot {
    private String userId;
    private String name;
    private int age;
    private boolean verified;
    private float distance;
    private String city;
    private boolean online;
    private List<String> images;
    private boolean likedMe;
}


