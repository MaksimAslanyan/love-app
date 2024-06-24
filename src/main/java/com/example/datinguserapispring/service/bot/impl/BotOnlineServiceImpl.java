package com.example.datinguserapispring.service.bot.impl;


import com.example.datinguserapispring.model.entity.bot.BotOnlineStatus;
import com.example.datinguserapispring.repository.BotOnlineStatusRepository;
import com.example.datinguserapispring.service.bot.BotOnlineStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class BotOnlineServiceImpl implements BotOnlineStatusService {
    private static final int BATCH_SIZE = 100;

    private final BotOnlineStatusRepository botOnlineStatusRepository;


    @Override
    public void setInactiveBots() {
        toggleBotStatus(false);
    }

    @Override
    public void setActiveBots() {
        toggleBotStatus(true);
    }

    private void toggleBotStatus(boolean isOnline) {
        long countStatuses;
        if (isOnline) {
            countStatuses = botOnlineStatusRepository.countByIsOnlineFalse();

        } else {
            countStatuses = botOnlineStatusRepository.countByIsOnlineTrue();
        }

        countStatuses /= 2;

        botOnlineStatusRepository.updateRandomBotStatuses(isOnline, countStatuses);

    }
}

