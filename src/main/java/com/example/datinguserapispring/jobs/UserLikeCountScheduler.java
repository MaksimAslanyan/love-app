//package com.example.datinguserapispring.jobs;
//
//import com.example.datinguserapispring.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//@Component
//@RequiredArgsConstructor
//public class UserLikeCountScheduler {
//
//    private final UserRepository userRepository;
//
//    @Transactional
//    @Scheduled(cron = "0 0 0 * * *") // Every day at 00:00:00
//    public void resetLikeCountsDaily() {
//        userRepository.resetLikeCounts();
//    }
//}
