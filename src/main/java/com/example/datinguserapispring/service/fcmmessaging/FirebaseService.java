package com.example.datinguserapispring.service.fcmmessaging;

import com.example.datinguserapispring.dto.messaging.DeviceTokenRequest;
import com.example.datinguserapispring.dto.messaging.NotificationDto;
import com.example.datinguserapispring.model.entity.bot.Bot2;
import com.example.datinguserapispring.model.entity.user.User;

public interface FirebaseService {

    boolean saveUserToken(DeviceTokenRequest note);

    void sendNotificationToUser(User receiverUser, String name);

    boolean sendNotification(NotificationDto notificationDto);

    void sendLikeNotification(String userToken, Bot2 bot2);

    void sendMatchNotification(String userToken);

}
