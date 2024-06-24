package com.example.datinguserapispring.service.bot;

import com.example.datinguserapispring.dto.bot.request.*;
import com.example.datinguserapispring.dto.bot.response.*;
import com.example.datinguserapispring.model.entity.Admin;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BotService {
    CreateBot2Response createBot2(CreateBot2Request request, Admin admin);

    List<Bot1DTO> listBot1(Pageable pageable);

    List<BotProfileSnapshotForAdmin> listBot2(Pageable pageable);

    CreateBot1Response createBot1(CreateBot1Request request);

    DeleteBotResponse deleteBot2(String botId);

    List<BotProfileSnapshotForAdmin> getBotsPhase2ByBot1Id(BotSearchCriteriaRequest dto, Pageable pageable);

    EditBot2Response editBot2(String id, EditBot2Request request);

    BotProfileSnapshotForAdmin getBotPhase2ById(String id);

    AddBot1ToAdminResponse addBot1ToAdmin(AddBot1ToAdminRequest addBot1ToAdminRequest);
}
