package com.example.datinguserapispring.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public interface SecurityContextService {


    AppUserDetails getUserDetails();

    void setAuthentication(UsernamePasswordAuthenticationToken auth);

    void clearAuthentication();
}
