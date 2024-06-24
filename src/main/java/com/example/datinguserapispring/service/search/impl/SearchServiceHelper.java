package com.example.datinguserapispring.service.search.impl;

import com.example.datinguserapispring.dto.location.LatLonDTO;
import com.example.datinguserapispring.model.entity.user.User;

public abstract class SearchServiceHelper {
     abstract double getOpponentDistance(LatLonDTO myPlaceDTO, User user);
}
