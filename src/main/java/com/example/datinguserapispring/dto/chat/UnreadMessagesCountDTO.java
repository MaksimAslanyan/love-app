package com.example.datinguserapispring.dto.chat;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnreadMessagesCountDTO {
    private String chatType;
    private BigInteger counts;
}
