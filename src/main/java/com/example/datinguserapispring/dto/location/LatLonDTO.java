package com.example.datinguserapispring.dto.location;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class LatLonDTO {
    private double lat;
    private double lon;
}
