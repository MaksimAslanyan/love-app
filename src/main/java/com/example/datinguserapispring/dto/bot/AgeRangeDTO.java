package com.example.datinguserapispring.dto.bot;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AgeRangeDTO {
    private byte minAge;
    private byte maxAge;
}
