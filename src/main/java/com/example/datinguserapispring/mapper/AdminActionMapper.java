package com.example.datinguserapispring.mapper;


import com.example.datinguserapispring.dto.admin.AdminActionLogDTO;
import com.example.datinguserapispring.model.entity.user.AdminAction;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdminActionMapper {

    AdminAction mapToEntity(AdminActionLogDTO adminActionLogDTO);
}
