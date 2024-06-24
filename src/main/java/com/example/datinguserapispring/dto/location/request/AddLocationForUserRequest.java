package com.example.datinguserapispring.dto.location.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class AddLocationForUserRequest {
    private double lat;
    private double lon;
    private String timeZone;
}
