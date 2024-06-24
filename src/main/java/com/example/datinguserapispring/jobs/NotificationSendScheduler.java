//package com.example.datinguserapispring.jobs;
//
//import com.example.datinguserapispring.model.entity.Notification;
//import com.example.datinguserapispring.model.entity.bot.Bot2;
//import com.example.datinguserapispring.repository.Bot2Repository;
//import com.example.datinguserapispring.repository.NotificationRepository;
//import com.example.datinguserapispring.service.fcmmessaging.FirebaseService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Component
//@Slf4j
//@RequiredArgsConstructor
//public class NotificationSendScheduler {
//
//    private final FirebaseService firebaseService;
//    private final NotificationRepository notificationRepository;
//    private final Bot2Repository bot2Repository;
//
//    @Scheduled(fixedRate = 15000) // Run every 15 sec
//    @Async("asyncTaskExecutor")
//    public void processNotifications() {
//        List<Notification> notificationsToSend = notificationRepository
//                .findByIsSendFalseAndSendTimeLessThanEqual(LocalDateTime.now());
//        if (!notificationsToSend.isEmpty()) {
//            for (Notification notification : notificationsToSend) {
//                processSingleNotification(notification);
//            }
//        }
//    }
//
//    private void processSingleNotification(Notification notification) {
//        notification.setSend(true);
//        notificationRepository.save(notification);
//        Bot2 bot2 = bot2Repository
//                .findById(notification.getOpponentId()).orElse(null);
//        log.info("Bot with ID " + bot2.getId() + "Like and Match with user ID " + notification.getUser().getId());
//        String userToken = notification.getUser().getToken();
//        boolean isActive = notification.getUser().isActive();
//        boolean isBlocked = notification.getUser().isBlackList();
//
//        if (userToken != null && isActive && !isBlocked) {
//            firebaseService.sendLikeNotification(userToken, bot2);
//            firebaseService.sendMatchNotification(userToken);
//
//        } else {
//            log.error("User token is null");
//        }
//
//
//    }
//
//}
