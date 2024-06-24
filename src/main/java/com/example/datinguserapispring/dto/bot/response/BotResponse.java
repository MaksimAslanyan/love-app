package com.example.datinguserapispring.dto.bot.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BotResponse {

    public BotResponse(String id) {
        this.id = id;
    }
    @JsonProperty("id")
    private String id;
}
