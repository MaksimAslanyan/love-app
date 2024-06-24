package com.example.datinguserapispring.dto.chat;


import com.example.datinguserapispring.dto.chat.response.AdminChatResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class AdminSortedChat {
    private List<AdminChatResponse> sortedList = new ArrayList<>();
    private long globalUnreadChatMessages;
}
