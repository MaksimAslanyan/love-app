package com.example.datinguserapispring.repository;

import com.example.datinguserapispring.dto.location.LatLonDTO;
import com.example.datinguserapispring.dto.search.request.SearchByCriteria;
import com.example.datinguserapispring.model.entity.user.User;

import java.util.List;

public interface SearchRepository {
    List<User> searchUserByCriteria(LatLonDTO myPlace, SearchByCriteria criteria);
}
