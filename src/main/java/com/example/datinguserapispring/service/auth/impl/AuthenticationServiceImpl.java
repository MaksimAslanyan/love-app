package com.example.datinguserapispring.service.auth.impl;

import com.example.datinguserapispring.apple.handler.AppleHandler;
import com.example.datinguserapispring.dto.apple.AppleIDTokenPayload;
import com.example.datinguserapispring.dto.auth.request.AuthAppleRequest;
import com.example.datinguserapispring.dto.auth.response.AuthResponse;
import com.example.datinguserapispring.exception.Error;
import com.example.datinguserapispring.exception.UserInBlackListException;
import com.example.datinguserapispring.manager.JwtTokenManager;
import com.example.datinguserapispring.model.entity.user.OnlineStatus;
import com.example.datinguserapispring.model.entity.user.User;
import com.example.datinguserapispring.model.enums.Gender;
import com.example.datinguserapispring.repository.BlackListRepository;
import com.example.datinguserapispring.repository.OnlineStatusRepository;
import com.example.datinguserapispring.repository.UserRepository;
import com.example.datinguserapispring.service.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtTokenManager jwtTokenManager;
    private final UserRepository userRepository;
    private final BlackListRepository blackListRepository;
    private final OnlineStatusRepository onlineStatusRepository;
    private final AppleHandler appleHandler;

    @Transactional
    public AuthResponse loginApple(AuthAppleRequest request) {
//        log.info("Logging in with Apple...");
//        AppleIDTokenPayload payload = appleHandler.appleAuth(request.getAuthCode());
//        String appleId = payload.getSub();
//        isBlackList(appleId);
//        Optional<User> byAppleId = userRepository.findFirstByAppleId(appleId);
//        User user;
//        boolean existUser = false;
//        OnlineStatus onlineStatus;
//        if (byAppleId.isPresent() && byAppleId.get().isActive()) {
//            log.info("Logging in with existing active user Apple ID: {}", appleId);
//            existUser = true;
//            user = byAppleId.get();
//        } else if (byAppleId.isPresent() && !byAppleId.get().isActive()) {
//            log.info("Reactivating user with Apple ID: {}", appleId);
//            user = byAppleId.get();
//            user.setActive(true);
//            userRepository.save(user);
//        } else {
//            log.info("Creating a new user with Apple ID: {}", appleId);
//            user = createUser(appleId);
//            onlineStatus = createOnlineStatus(user);
//
//            onlineStatusRepository.save(onlineStatus);
//            userRepository.save(user);
//        }

        String token = jwtTokenManager.generateTokenForApple("2d72443e-7b6e-4824-a515-b79e1414a2ca");
        return new AuthResponse(token, true);
    }

    private void isBlackList(String appleId) {
        boolean isBlackList = blackListRepository.existsByAppleId(appleId);
        if (isBlackList) {
            throw new UserInBlackListException(Error.BLOCKED_USER);
        }
    }

    private OnlineStatus createOnlineStatus(User user) {
        OnlineStatus onlineStatus;
        onlineStatus = OnlineStatus.builder()
                .user(user)
                .isOnline(true)
                .build();
        return onlineStatus;
    }

    private User createUser(String appleId) {
        User user;
        user = User.builder()
                .appleId(appleId)
                .createdAt(LocalDateTime.now())
                .lastActivity(LocalDateTime.now())
                .roles("USER")
                .dob(LocalDate.now())
                .language("en")
                .lookingFor(List.of())
                .gender(Gender.NOT_CHOSEN)
                .targetGender("")
                .age(0)
                .userName(appleId)
                .isPremium(false)
                .isActive(true)
                .build();
        return user;
    }
}
