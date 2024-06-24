package com.example.datinguserapispring.api.controller;


import com.example.datinguserapispring.dto.premium.request.PremiumPeriodRequest;
import com.example.datinguserapispring.dto.premium.response.PremiumPeriodResponse;
import com.example.datinguserapispring.security.SecurityContextService;
import com.example.datinguserapispring.service.premium.PremiumPeriodService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PremiumPeriodController {

    private final PremiumPeriodService premiumPeriodService;
    private final SecurityContextService securityContextService;

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/users/premium")
    public PremiumPeriodResponse addPremiumUser(@RequestBody PremiumPeriodRequest period) {
        return premiumPeriodService.save(securityContextService.getUserDetails().getUsername(), period.getDateUntil());
    }
}
