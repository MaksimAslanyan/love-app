package com.example.datinguserapispring.dto.bot.response;


import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DeleteBotResponse extends BotResponse{
    public DeleteBotResponse(String id) {
        super(id);
    }
}
