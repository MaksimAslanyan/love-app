package com.example.datinguserapispring.mapper;


import com.example.datinguserapispring.dto.premium.response.PremiumPeriodResponse;
import com.example.datinguserapispring.model.entity.user.PremiumPeriod;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PremiumPeriodMapper {

    PremiumPeriodResponse mapToDto(PremiumPeriod premiumPeriod);

}
