package com.example.datinguserapispring.dto.bot.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class AddBot1ToAdminRequest {
    private String adminId;
    private String bot1Id;
}
