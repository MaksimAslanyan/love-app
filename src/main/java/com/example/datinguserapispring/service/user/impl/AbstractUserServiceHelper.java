package com.example.datinguserapispring.service.user.impl;

import com.example.datinguserapispring.dto.user.response.ProfileSnapshot;
import com.example.datinguserapispring.dto.user.response.ProfileSnapshotForAdminResponse;
import com.example.datinguserapispring.dto.user.response.UserInfoResponse;
import com.example.datinguserapispring.dto.photo.response.PhotoDTO;
import com.example.datinguserapispring.model.entity.user.User;

import java.util.List;

abstract class AbstractUserServiceHelper {


    abstract UserInfoResponse buildUserInfoResponse(User user, List<PhotoDTO> photoUrls);

    abstract ProfileSnapshotForAdminResponse converter(User user);

    abstract ProfileSnapshot getProfileSnapshot(User user);

    abstract ProfileSnapshot buildUserProfileSnapshotResponse(User user, List<PhotoDTO> photoUrls);
}
