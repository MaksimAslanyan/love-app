package com.example.datinguserapispring.dto.block.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BlockRequest {
    private String blockedUserId;
}
