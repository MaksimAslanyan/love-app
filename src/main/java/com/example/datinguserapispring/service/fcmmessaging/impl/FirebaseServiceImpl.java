package com.example.datinguserapispring.service.fcmmessaging.impl;

import com.example.datinguserapispring.dto.messaging.DeviceTokenRequest;
import com.example.datinguserapispring.dto.messaging.NotificationDto;
import com.example.datinguserapispring.exception.Error;
import com.example.datinguserapispring.exception.FirebaseException;
import com.example.datinguserapispring.model.entity.bot.Bot2;
import com.example.datinguserapispring.model.entity.user.User;
import com.example.datinguserapispring.repository.UserRepository;
import com.example.datinguserapispring.security.SecurityContextService;
import com.example.datinguserapispring.service.fcmmessaging.FirebaseService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional(rollbackFor = FirebaseException.class)
public class FirebaseServiceImpl implements FirebaseService {

    public final static String CANT_SEND = "CANT_SEND";

    private final SecurityContextService securityContextService;
    private final UserRepository userRepository;
    private final FirebaseMessaging firebaseMessaging;

    @Override
    public boolean saveUserToken(DeviceTokenRequest deviceTokenRequest) {
        userRepository.updateUserToken(deviceTokenRequest.getToken(),
                securityContextService.getUserDetails().getUsername());
        return true;
    }

    @Override
    public void sendNotificationToUser(User receiverUser, String name) {
        if (receiverUser != null && name != null) {
            String notificationContent = name + " wrote you. Read it now!";
            sendNotification(new NotificationDto(receiverUser.getToken(),
                    "Someone wants to talk to you", notificationContent));
        }
    }

    @Override
    @Transactional
    public boolean sendNotification(NotificationDto notificationDto) {
        log.info(notificationDto.getContent());
        Notification notification = Notification
                .builder()
                .setTitle(notificationDto.getSubject())
                .setBody(notificationDto.getContent())
                .build();

        Message message = Message
                .builder()
                .setToken(notificationDto.getToken())
                .setNotification(notification)
                .build();
        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            log.info("ERROR FIREBASE");
        }
        return true;
    }

    @Override
    public void sendLikeNotification(String userToken, Bot2 bot2) {
        if (bot2 != null) {
            log.info("Send Like Notification to User");
            if(userToken != null){
                sendNotification(new NotificationDto(
                        userToken,
                        "Someone liked you",
                        bot2.getNameBot() + " liked you"
                ));

            }
        }
        log.info("Like Notification sent");
    }

    @Override
    public void sendMatchNotification(String userToken) {
        log.info("Send Match Notification to User");
        sendNotification(
                new NotificationDto(userToken,
                "New match",
                "We found a new match for you!"
        ));
        log.info("Match Notification sent");
    }

}
