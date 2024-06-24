//package com.example.datinguserapispring.jobs;
//
//
//import com.example.datinguserapispring.service.bot.BotOnlineStatusService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class BotOnlineStatusScheduler {
//
//    private final BotOnlineStatusService botOnlineStatusService;
//
////    @Scheduled(fixedDelay = 1200000) // 20 minutes in milliseconds
//    @Async
//    public void setBotInactivityStatus() {
//        botOnlineStatusService.setInactiveBots();
//    }
//
////    @Scheduled(fixedDelay = 1200000) // 20 minutes in milliseconds
//    @Async
//    public void updateBotOnlineStatus() {
//        botOnlineStatusService.setActiveBots();
//    }
//}
//
