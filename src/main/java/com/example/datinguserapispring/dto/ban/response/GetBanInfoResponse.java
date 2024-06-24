package com.example.datinguserapispring.dto.ban.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class GetBanInfoResponse {
    private boolean isBlocked;
    private LocalDate dateUntil;
    private Boolean isForever;
    private String reason;
}
