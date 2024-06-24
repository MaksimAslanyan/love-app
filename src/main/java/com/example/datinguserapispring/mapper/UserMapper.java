package com.example.datinguserapispring.mapper;


import com.example.datinguserapispring.dto.user.response.ProfileSnapshot;
import com.example.datinguserapispring.model.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    ProfileSnapshot mapToProfileSnapshot(User user);

}
