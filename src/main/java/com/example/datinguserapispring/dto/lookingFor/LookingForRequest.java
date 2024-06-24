package com.example.datinguserapispring.dto.lookingFor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class LookingForRequest {
    private List<String> lookingForList;
}
