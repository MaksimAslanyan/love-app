package com.example.datinguserapispring.service.search.impl;

import com.example.datinguserapispring.dto.location.LatLonDTO;
import com.example.datinguserapispring.mapper.LocationMapper;
import com.example.datinguserapispring.model.entity.Location;
import com.example.datinguserapispring.model.entity.user.User;
import com.example.datinguserapispring.repository.LocationRepository;
import com.example.datinguserapispring.util.CalculateDistanceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class SearchServiceHelperImpl extends SearchServiceHelper {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    double getOpponentDistance(LatLonDTO myPlaceDTO, User user) {
        List<Location> locationByUserId = locationRepository.findLocationByUserId(user.getId());
        LatLonDTO opponentPlace = locationMapper.mapToDTO(locationByUserId.get(0));
        return CalculateDistanceUtil.calculateDistance(myPlaceDTO, opponentPlace);
    }
}
