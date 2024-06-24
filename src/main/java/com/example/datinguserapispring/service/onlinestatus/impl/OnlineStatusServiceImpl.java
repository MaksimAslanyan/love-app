package com.example.datinguserapispring.service.onlinestatus.impl;

import com.example.datinguserapispring.dto.onlinestatus.request.OnlineStatusRequest;
import com.example.datinguserapispring.dto.onlinestatus.response.OnlineStatusResponse;
import com.example.datinguserapispring.exception.Error;
import com.example.datinguserapispring.exception.UserNotFoundException;
import com.example.datinguserapispring.model.entity.Admin;
import com.example.datinguserapispring.model.entity.user.OnlineStatus;
import com.example.datinguserapispring.model.entity.user.User;
import com.example.datinguserapispring.repository.AdminRepository;
import com.example.datinguserapispring.repository.OnlineStatusRepository;
import com.example.datinguserapispring.repository.UserRepository;
import com.example.datinguserapispring.security.SecurityContextService;
import com.example.datinguserapispring.service.bot.GenerateBotService;
import com.example.datinguserapispring.service.onlinestatus.OnlineStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@Slf4j
@RequiredArgsConstructor
public class OnlineStatusServiceImpl implements OnlineStatusService {

    private final OnlineStatusRepository onlineStatusRepository;
    private final SecurityContextService securityContextService;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final GenerateBotService generateBotService;



    @Transactional
    @Override
    public OnlineStatusResponse onlineStatusActivityForUser(OnlineStatusRequest request) {
        String id = securityContextService.getUserDetails().getUsername();
        log.info("Checking online status for user with ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(Error.USER_NOT_FOUND));
        
        OnlineStatus userOnlineActivity = onlineStatusRepository.findAllByUser(user)
                .orElseThrow(() -> new UserNotFoundException(Error.USER_NOT_FOUND));

        userOnlineActivity.setOnline(request.isOnline());
        userOnlineActivity.setLastOnlineTime(LocalDateTime.now());

        log.info("Updating online status for user with ID: {}", user.getId());

        userOnlineActivity = onlineStatusRepository.save(userOnlineActivity);
        return new OnlineStatusResponse(userOnlineActivity.getUser().getId());
    }

    public OnlineStatusResponse onlineStatusActivityForAdmin(OnlineStatusRequest request) {
        String login = securityContextService.getUserDetails().getUsername();
        log.info("Checking online status for admin with login: {}", login);

        Admin admin = adminRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(Error.USER_NOT_FOUND));

        OnlineStatus adminOnlineActivity = onlineStatusRepository.findAllByAdmin(admin)
                .orElseThrow(() -> new UserNotFoundException(Error.USER_NOT_FOUND)).getOnlineStatus();

        adminOnlineActivity.setOnline(request.isOnline());
        adminOnlineActivity.setLastOnlineTime(LocalDateTime.now());

        log.info("Updating online status for user with ID: {}", admin.getId());

        adminOnlineActivity = onlineStatusRepository.save(adminOnlineActivity);
        return new OnlineStatusResponse(adminOnlineActivity.getUser().getId());
    }
}
