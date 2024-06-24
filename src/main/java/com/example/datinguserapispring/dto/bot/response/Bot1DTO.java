package com.example.datinguserapispring.dto.bot.response;

import com.example.datinguserapispring.model.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class Bot1DTO {
    private String id;
    private Gender gender;
    private String race;
    private byte minAge;
    private byte maxAge;
}
