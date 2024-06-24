package com.example.datinguserapispring.dto.user.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class FilterUsersByGender {
    private String gender;
    private String id;
}
