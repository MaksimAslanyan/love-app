package com.example.datinguserapispring.api.controller;


import com.example.datinguserapispring.dto.auth.request.AuthAppleRequest;
import com.example.datinguserapispring.dto.auth.response.AuthResponse;
import com.example.datinguserapispring.service.auth.impl.AuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationServiceImpl authService;


    @PostMapping("/loginApple")
    public AuthResponse login(@RequestBody AuthAppleRequest request) {
        return authService.loginApple(request);
    }


}
