//package com.example.datinguserapispring.jobs;
//
//import com.example.datinguserapispring.model.entity.bot.Bot2;
//import com.example.datinguserapispring.model.entity.user.Address;
//import com.example.datinguserapispring.model.entity.user.User;
//import com.example.datinguserapispring.model.entity.user.UserLike;
//import com.example.datinguserapispring.repository.AddressRepository;
//import com.example.datinguserapispring.repository.Bot2Repository;
//import com.example.datinguserapispring.repository.BotOnlineStatusRepository;
//import com.example.datinguserapispring.repository.LookingForRepository;
//import com.example.datinguserapispring.repository.PhotoRepository;
//import com.example.datinguserapispring.repository.UserLikeRepository;
//import com.example.datinguserapispring.repository.UserRepository;
//import com.example.datinguserapispring.service.bot.GenerateBotService;
//import com.example.datinguserapispring.service.fcmmessaging.FirebaseService;
//import com.example.datinguserapispring.service.photo.PhotoService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.context.event.EventListener;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static java.util.stream.Collectors.toList;
//
//@Component
//@Slf4j
//@RequiredArgsConstructor
//public class BotLikeScheduler {
//    private static final int PAGE_SIZE = 100;
//
//    private final AddressRepository addressRepository;
//    private final UserRepository userRepository;
//    private final UserLikeRepository userLikeRepository;
//    private final FirebaseService firebaseService;
//    private final GenerateBotService generateBotService;
//
//    @Scheduled(cron = "0 0 */2 * * *") // Run every 2 hours
//    @Async("asyncTaskExecutor")
//    public void provideLikesFirstPhaseScheduler2() {
//        processUsersForScheduler2();
//    }
//
//    private void processUsersForScheduler2() {
//        // Use a unique scheduler name for each scheduler instance
//        String SCHEDULER_NAME_PREFIX = "provideLikesPhase|2|";
//        log.info("SCHEDULED TASK '{}' STARTED.", SCHEDULER_NAME_PREFIX);
//
//        long userCount = userRepository.countAllActiveUsersWithTokenAndNotBlackListedAndTargetGenderOrNotChosen();
//        int totalPages = (int) Math.ceil((double) userCount / PAGE_SIZE);
//
//        for (int pageNumber = 0; pageNumber <= totalPages; pageNumber++) {
//            Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE);
//            Page<User> userPage = userRepository.findAllActiveUsersWithTokenAndNotBlackListedAndTargetGenderOrNotChosenDesc(pageable);
//            List<User> users = userPage.getContent();
//            log.info("PROCESSING |2| '{}' PAGE {} OF USERS. TOTAL USERS ON THIS PAGE: {}", SCHEDULER_NAME_PREFIX, pageNumber, users.size());
//
//            log.info("<------------------------------- START PROCESSING PAGE {} ------------------------------------------->", pageNumber);
//
//            List<User> filteredUsers = users.stream()
//                    .filter(user -> user.getLikeCount() <= 20 && user.getToken() != null)
//                    .filter(user -> {
//                        List<Address> allByUser = addressRepository.findAllByUser(user);
//                        return !allByUser.isEmpty() &&
//                                allByUser.get(0).getCity() != null &&
//                                allByUser.get(0).getCountry() != null;
//                    })
//                    .collect(toList());
//
//            List<Bot2> generatedBots = generateBotService.generateBots(filteredUsers);
//            createUserLike(generatedBots);
//
//            log.info("<------------------------- FINISHED PROCESSING |2| PAGE {} -------------------------------------->", pageNumber);
//        }
//        log.info("SCHEDULED TASK |2| COMPLETED.");
//    }
//
//    private double calculateBotActivityForUser(User user) {
//        LocalDateTime now = LocalDateTime.now();
//        double botActivityForUser = 0;
//
//        if (user.getOnlineStatus().getLastOnlineTime().plusDays(1).isBefore(now) ||
//                user.getOnlineStatus().getLastOnlineTime().plusDays(2).isBefore(now)) {
//            botActivityForUser = 0.75;
//        } else if (user.getOnlineStatus().getLastOnlineTime().plusDays(3).isBefore(now) ||
//                user.getOnlineStatus().getLastOnlineTime().plusDays(6).isBefore(now)) {
//            botActivityForUser = 0.50;
//        } else if (user.getOnlineStatus().getLastOnlineTime().plusDays(7).isBefore(now)) {
//            botActivityForUser = 0.25;
//        } else if (user.getOnlineStatus().getLastOnlineTime().plusDays(14).isBefore(now)) {
//            botActivityForUser = 0;
//        }
//
//        return botActivityForUser;
//    }
//
//    private void createUserLike(List<Bot2> bots) {
//        log.info("Create User Like");
//
//        for (Bot2 bot : bots) {
//            User user = bot.getUser();
//            UserLike newLike = UserLike.builder()
//                    .user(user)
//                    .bot2(bot)
//                    .userAuthorType("BOT")
//                    .isLikedYou(true)
//                    .createdAt(LocalDateTime.now())
//                    .build();
//            log.info("saving Scheduler like " + newLike);
//            firebaseService.sendLikeNotification(user.getToken(), bot);
//            user.setLikeCount(user.getLikeCount() + 1);
//            userRepository.save(user);
//            userLikeRepository.save(newLike);
//            log.info("saving new like to user name " + user.getName());
//        }
//    }
//}
