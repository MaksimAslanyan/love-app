package com.example.datinguserapispring.mapper;


import com.example.datinguserapispring.dto.location.LatLonDTO;
import com.example.datinguserapispring.model.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LocationMapper {

    LatLonDTO mapToDTO(Location location);
}
