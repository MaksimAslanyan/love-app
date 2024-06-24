package com.example.datinguserapispring.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextServiceImpl implements SecurityContextService {


    @Override
    public AppUserDetails getUserDetails() {
        return (AppUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public void setAuthentication(UsernamePasswordAuthenticationToken auth) {
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Override
    public void clearAuthentication() {
        SecurityContextHolder.clearContext();
    }
}
