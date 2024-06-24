package com.example.datinguserapispring.dto.ban.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class BanInfoRequest {
    private LocalDate dateUntil;
    private boolean isForever;
    private String reason;
}
