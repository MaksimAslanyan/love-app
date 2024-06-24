package com.example.datinguserapispring.dto.fetch.request;

import com.example.datinguserapispring.model.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FetchRequest {
    private Gender gender;
    private String appleId;
    private String adminId;
}