package com.example.datinguserapispring.dto.chat;


import com.example.datinguserapispring.model.entity.chat.Chat;
import com.example.datinguserapispring.model.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class AdminChatDTO {
    private Chat chat;
    private String bot2Id;
    private String nameBot;
    private String adminId;
    private String bot1Id;
    private Gender gender;
    private String race;
    private byte minAge;
    private byte maxAge;
    private String userId;
    private String userName;
    private Gender userGender;
    private boolean isPremium;
    private boolean isBlackList;
}
