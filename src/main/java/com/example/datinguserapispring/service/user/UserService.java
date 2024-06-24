package com.example.datinguserapispring.service.user;

import com.example.datinguserapispring.dto.user.request.PatchProfileRequest;
import com.example.datinguserapispring.dto.user.response.*;
import com.example.datinguserapispring.dto.fetch.request.FetchRequest;
import com.example.datinguserapispring.security.AppUserDetails;

import java.util.List;

public interface UserService {
    UsersCountResponse getUsersCount();

    ProfileSnapshot getProfileSnapshot(String username);

    ProfileSnapshot patchProfile(AppUserDetails userDetails, PatchProfileRequest request);

    List<ProfileSnapshotForAdminResponse> getAllUsers(FetchRequest fetchRequest, int page, int size);

    UserInfoResponse getUserById(String id);

    DeactivateProfile deactivate(AppUserDetails userDetails, String username);
}
