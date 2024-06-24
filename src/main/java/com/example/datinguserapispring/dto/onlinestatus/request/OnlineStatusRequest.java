package com.example.datinguserapispring.dto.onlinestatus.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class OnlineStatusRequest {
    @JsonProperty("isOnline")
    private boolean isOnline;
}
