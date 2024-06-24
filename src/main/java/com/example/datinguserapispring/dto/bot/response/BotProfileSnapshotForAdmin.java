package com.example.datinguserapispring.dto.bot.response;

import com.example.datinguserapispring.dto.photo.response.PhotoDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class BotProfileSnapshotForAdmin {
    @JsonProperty("id")
    private String id;
    @JsonProperty("nameBot")
    private String nameBot;
    @JsonProperty("adminId")
    private String adminId;
    @JsonProperty("isBotBlocked")
    private boolean isBotBlocked;
    @JsonProperty("bot1")
    private Bot1DTO bot1;
    @JsonProperty("photoUrls")
    private List<PhotoDTO> photoUrls;
}
