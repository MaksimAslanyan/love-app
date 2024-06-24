package com.example.datinguserapispring.api.controller;


import com.example.datinguserapispring.dto.onlinestatus.request.OnlineStatusRequest;
import com.example.datinguserapispring.dto.onlinestatus.response.OnlineStatusResponse;
import com.example.datinguserapispring.service.onlinestatus.OnlineStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OnlineStatusController {

    private final OnlineStatusService onlineStatusService;


    @PostMapping("/is-online")
    public OnlineStatusResponse onlineStatusActivityForUser(@RequestBody OnlineStatusRequest request) {
        return onlineStatusService.onlineStatusActivityForUser(request);
    }

    @PostMapping("/admin/is-online")
    public OnlineStatusResponse onlineStatusActivityForAdmin(@RequestBody OnlineStatusRequest request) {
        return onlineStatusService.onlineStatusActivityForAdmin(request);
    }
}
