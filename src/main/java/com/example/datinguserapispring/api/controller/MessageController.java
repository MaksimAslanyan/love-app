package com.example.datinguserapispring.api.controller;

import com.example.datinguserapispring.dto.messaging.DeviceTokenRequest;
import com.example.datinguserapispring.service.fcmmessaging.FirebaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final FirebaseService firebaseService;

    @PostMapping("/send-notification")
    @ResponseBody
    public boolean sendNotification(@RequestBody DeviceTokenRequest note){
        return firebaseService.saveUserToken(note);
    }
}
