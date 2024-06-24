package com.example.datinguserapispring.dto.chat.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class UnreadMessagesCountsResponse {
    private long premium;
    private long lesbian;
    private long gay;
    private long inactive;
    private long maleFemale;
    private long femaleMale;
}
