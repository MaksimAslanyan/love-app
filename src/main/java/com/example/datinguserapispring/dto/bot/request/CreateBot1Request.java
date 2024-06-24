package com.example.datinguserapispring.dto.bot.request;

import com.example.datinguserapispring.model.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class CreateBot1Request {
    private Gender gender;
    private byte minAge;
    private byte maxAge;
    private String race;
}

       
