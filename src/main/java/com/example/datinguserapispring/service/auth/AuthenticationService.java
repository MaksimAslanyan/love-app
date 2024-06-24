package com.example.datinguserapispring.service.auth;

import com.example.datinguserapispring.dto.auth.request.AuthAppleRequest;
import com.example.datinguserapispring.dto.auth.response.AuthResponse;

public interface AuthenticationService {
    AuthResponse loginApple(AuthAppleRequest request);

}
