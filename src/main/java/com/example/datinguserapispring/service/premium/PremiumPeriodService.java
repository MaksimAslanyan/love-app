package com.example.datinguserapispring.service.premium;

import com.example.datinguserapispring.dto.premium.response.PremiumPeriodResponse;

import java.time.LocalDateTime;

public interface PremiumPeriodService {

    PremiumPeriodResponse save(String userId, LocalDateTime period);



}
