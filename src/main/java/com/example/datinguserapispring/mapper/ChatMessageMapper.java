package com.example.datinguserapispring.mapper;

import com.example.datinguserapispring.dto.chat.MessageContent;
import com.example.datinguserapispring.dto.chat.MessageDTO;
import com.example.datinguserapispring.model.entity.chat.ChatMessage;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChatMessageMapper {

    MessageDTO mapToDTO(ChatMessage chatMessage);
    MessageContent mapToMessageContent(ChatMessage chatMessage);
}
