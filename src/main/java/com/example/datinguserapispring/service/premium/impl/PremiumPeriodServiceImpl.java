package com.example.datinguserapispring.service.premium.impl;

import com.example.datinguserapispring.dto.premium.response.PremiumPeriodResponse;
import com.example.datinguserapispring.exception.AuthenticationException;
import com.example.datinguserapispring.exception.EntityNotFoundException;
import com.example.datinguserapispring.exception.Error;
import com.example.datinguserapispring.mapper.PremiumPeriodMapper;
import com.example.datinguserapispring.model.entity.chat.Chat;
import com.example.datinguserapispring.model.entity.chat.ChatMember;
import com.example.datinguserapispring.model.entity.user.PremiumPeriod;
import com.example.datinguserapispring.model.entity.user.User;
import com.example.datinguserapispring.model.enums.ChatType;
import com.example.datinguserapispring.repository.ChatMemberRepository;
import com.example.datinguserapispring.repository.ChatRepository;
import com.example.datinguserapispring.repository.PremiumPeriodRepository;
import com.example.datinguserapispring.repository.UserRepository;
import com.example.datinguserapispring.service.premium.PremiumPeriodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class PremiumPeriodServiceImpl implements PremiumPeriodService {

    private final PremiumPeriodRepository premiumPeriodRepository;
    private final UserRepository userRepository;
    private final PremiumPeriodMapper premiumPeriodMapper;
    private final ChatMemberRepository chatMemberRepository;
    private final ChatRepository chatRepository;


    @Override
    @Transactional
    public PremiumPeriodResponse save(String userId, LocalDateTime period) {
        log.info("Saving premium period for user with ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthenticationException(Error.USER_NOT_FOUND));
        PremiumPeriod premiumPeriod = new PremiumPeriod(user, LocalDateTime.now(), period);
        boolean hasActivePremiums = premiumPeriodRepository
                .existsAllByUserIdAndActive(user.getId(), true);

        premiumPeriod.setActive(!hasActivePremiums);
        user.setPremium(true);

        premiumPeriodMapper.mapToDto(premiumPeriodRepository.save(premiumPeriod));
        log.info("Premium period saved successfully for user with ID: {}", userId);
        changeChatsTypeToPremium(userId);

        return new PremiumPeriodResponse(userId);
    }

    @Async
    public CompletableFuture<Void> changeChatsTypeToPremium(String userId) {
        log.info("Attempting to change chat type to premium for user with ID: " + userId);

        List<ChatMember> chatsMembers = chatMemberRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(Error.ENTITY_NOT_FOUND));

        List<Chat> chatsToUpdate = chatsMembers.stream()
                .map(ChatMember::getChat)
                .filter(chat -> !chat.getChatType().equals(ChatType.NULL))
                .peek(chat -> {
                    chat.setChatType(ChatType.IS_PREMIUM);
                })
                .toList();

        chatRepository.saveAll(chatsToUpdate);
        log.info("Successfully updated " + chatsToUpdate.size() + " chats to premium type.");
        return CompletableFuture.completedFuture(null);
    }
}
