package com.example.datinguserapispring.dto.user.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class UsersCountResponse {
    private long count;
    private long maleCount;
    private long femaleCount;
}
