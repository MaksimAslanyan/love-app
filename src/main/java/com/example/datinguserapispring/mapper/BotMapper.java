package com.example.datinguserapispring.mapper;


import com.example.datinguserapispring.dto.bot.response.Bot1DTO;
import com.example.datinguserapispring.model.entity.bot.Bot1;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BotMapper {
    Bot1DTO mapToBot1DTO(Bot1 bot1);
}
