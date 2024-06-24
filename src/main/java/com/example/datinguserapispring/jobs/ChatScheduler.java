//package com.example.datinguserapispring.jobs;
//
//
//import com.example.datinguserapispring.model.entity.user.User;
//import com.example.datinguserapispring.model.enums.ChatType;
//import com.example.datinguserapispring.model.entity.chat.Chat;
//import com.example.datinguserapispring.model.entity.chat.ChatMember;
//import com.example.datinguserapispring.model.entity.user.OnlineStatus;
//import com.example.datinguserapispring.repository.ChatMemberRepository;
//import com.example.datinguserapispring.repository.ChatRepository;
//import com.example.datinguserapispring.repository.OnlineStatusRepository;
//import com.example.datinguserapispring.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.util.Collections;
//import java.util.List;
//
//@Component
//@Slf4j
//@RequiredArgsConstructor
//public class ChatScheduler {
//
//    private final ChatMemberRepository chatMemberRepository;
//    private final ChatRepository chatRepository;
//    private final OnlineStatusRepository onlineStatusRepository;
//    private final UserRepository userRepository;
//
//
//    @Scheduled(cron = "0 0 1 * * ?")
//    @Async("asyncTaskExecutor")
//    public void setInactiveChats() {
//        log.info("Setting inactive chats...");
//        LocalDateTime fourteenDaysAgo = LocalDateTime.now().minusDays(14);
//        log.info("Setting inactive chats...1");
//        List<OnlineStatus> onlineStatusList = onlineStatusRepository.findByLastOnlineTimeBefore(fourteenDaysAgo);
//
//        for (OnlineStatus onlineStatus : onlineStatusList) {
//            try {
//                log.info("InForLoop");
//
//                User user = onlineStatus.getUser();
//                String userId;
//
//                if (user == null) {
//                    log.warn("OnlineStatus with null User found. Skipping processing.");
//                    continue;
//                }
//
//                userId = user.getId();  // Use the existing userId variable
//
//                User userEntity = userRepository.findById(userId).orElse(null);
//                log.info("Processing user with ID: {}", userId);
//
//                if (userEntity == null) {
//                    log.warn("User with ID {} not found.", userId);
//                    continue;
//                }
//
//                List<ChatMember> chatMembers = chatMemberRepository.findByUserId(userId).orElseGet(Collections::emptyList);
//
//                if (chatMembers.isEmpty()) {
//                    log.info("No chat members found for user with ID: {}", userId);
//                    continue;
//                }
//
//                for (ChatMember chatMember : chatMembers) {
//                    Chat chat = chatMember.getChat();
//                    chat.setChatType(ChatType.INACTIVE);
//                    log.info("Setting chat {} to inactive for user with ID: {}", chat.getId(), userId);
//                }
//
//                List<Chat> chatsToUpdate = chatMembers.stream()
//                        .map(ChatMember::getChat)
//                        .toList();
//
//                chatRepository.saveAll(chatsToUpdate);
//                log.info("Chats updated successfully for user with ID: {}", userId);
//            } catch (Exception e) {
//                log.error("Error processing user.", e);
//            }
//        }
//    }
//
//
//}
