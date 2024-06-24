package com.example.datinguserapispring.dto.search.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class SearchByCriteria {
    private int maxResults;
    private int radius;
    private byte ageFrom;
    private byte ageTo;
}
