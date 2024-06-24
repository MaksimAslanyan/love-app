//package com.example.datinguserapispring.jobs;
//
//
//import com.example.datinguserapispring.exception.EntityNotFoundException;
//import com.example.datinguserapispring.exception.Error;
//import com.example.datinguserapispring.model.entity.chat.Chat;
//import com.example.datinguserapispring.model.entity.chat.ChatMember;
//import com.example.datinguserapispring.model.entity.user.PremiumPeriod;
//import com.example.datinguserapispring.model.entity.user.User;
//import com.example.datinguserapispring.model.enums.ChatType;
//import com.example.datinguserapispring.model.enums.Gender;
//import com.example.datinguserapispring.repository.*;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import javax.transaction.Transactional;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Component
//@Slf4j
//@RequiredArgsConstructor
//public class PremiumPeriodCheckScheduler {
//
//
//    private final PremiumPeriodRepository premiumPeriodRepository;
//    private final UserRepository userRepository;
//    private final ChatMemberRepository chatMemberRepository;
//    private final ChatRepository chatRepository;
//    private final ChatMemberAdminRepository chatMemberAdminRepository;
//
//
//    @Scheduled(fixedDelay = 20 * 60 * 1000)
//    @Async("asyncTaskExecutor")
//    @Transactional
//    public void checkAndUpdatePremiumPeriods() {
//        log.info("Scheduled task to check and update premium periods started.");
//
//        LocalDateTime currentDateTime = LocalDateTime.now();
//        List<PremiumPeriod> expiredPremiumPeriods = premiumPeriodRepository
//                .findByUntilDateBeforeAndActiveTrue(currentDateTime);
//
//        for (PremiumPeriod premiumPeriod : expiredPremiumPeriods) {
//            log.info("Processing premium period with ID: {}", premiumPeriod.getId());
//
//            premiumPeriod.setActive(false);
//
//            User user = premiumPeriod.getUser();
//            LocalDateTime now = LocalDateTime.now();
//            boolean hasActivePremiumPeriods = premiumPeriodRepository
//                    .hasActivePremiumPeriods(user, now);
//
//            if (!hasActivePremiumPeriods) {
//                user.setPremium(false);
//                sortChatByOrientation(user);
//                log.info("User {} no longer has active premium periods. Premium status set to false.", user.getId());
//            } else {
//                PremiumPeriod latestPremiumPeriod = premiumPeriodRepository.findFirstByUserOrderByFromDateDesc(user);
//                latestPremiumPeriod.setActive(true);
//                user.setPremium(true);
//                premiumPeriodRepository.save(latestPremiumPeriod);
//                log.info("User {} has active premium periods. Latest premium period activated.", user.getId());
//            }
//
//            userRepository.save(user);
//            log.info("User {} information updated.", user.getId());
//        }
//
//        premiumPeriodRepository.saveAll(expiredPremiumPeriods);
//        log.info("Scheduled task to check and update premium periods completed.");
//    }
//
//    private void sortChatByOrientation(User user) {
//        String userId = user.getId();
//        Gender userGender = user.getGender();
//        List<ChatMember> chatsMembers = chatMemberRepository.findByUserId(userId)
//                .orElseThrow(() -> new EntityNotFoundException(Error.ENTITY_NOT_FOUND));
//
//        for (ChatMember chatMember : chatsMembers) {
//            Gender botGender = chatMember.getChat().getChatMemberAdmins().get(0).getBot2().getBot1().getGender();
//            Chat chat = chatMember.getChat();
//
//            if (userGender == Gender.FEMALE && botGender == Gender.FEMALE) {
//                chat.setChatType(ChatType.LESBIAN);
//                log.info("Chat {} updated to Lesbian type.", chat.getId());
//            } else if (userGender == Gender.MALE && botGender == Gender.MALE) {
//                chat.setChatType(ChatType.GAY);
//                log.info("Chat {} updated to Gay type.", chat.getId());
//            } else if (userGender == Gender.MALE && botGender == Gender.FEMALE) {
//                chat.setChatType(ChatType.MALE_FEMALE);
//                log.info("Chat {} updated to Male-Female type.", chat.getId());
//            } else if (userGender == Gender.FEMALE && botGender == Gender.MALE) {
//                chat.setChatType(ChatType.FEMALE_MALE);
//                log.info("Chat {} updated to Female-Male type.", chat.getId());
//            }
//
//            chatRepository.save(chat);
//            log.info("Chat {} information updated.", chat.getId());
//        }
//    }
//
//
//}
//
