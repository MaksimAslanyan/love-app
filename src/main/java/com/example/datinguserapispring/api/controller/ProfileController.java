package com.example.datinguserapispring.api.controller;

import com.example.datinguserapispring.dto.photo.request.DeletePhotoRequest;
import com.example.datinguserapispring.dto.photo.response.DeletePhotoResponse;
import com.example.datinguserapispring.dto.user.request.PatchProfileRequest;
import com.example.datinguserapispring.dto.user.response.DeactivateProfile;
import com.example.datinguserapispring.dto.user.response.ProfileSnapshot;
import com.example.datinguserapispring.security.AppUserDetails;
import com.example.datinguserapispring.security.SecurityContextService;
import com.example.datinguserapispring.service.photo.DeletePhotoService;
import com.example.datinguserapispring.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController("profileUser")
public class ProfileController {

    private final UserService userService;
    private final DeletePhotoService deletePhotoService;
    private final SecurityContextService securityContextService;


    @PatchMapping("/profile")
    @PreAuthorize("hasAuthority('USER')")
    public ProfileSnapshot patchProfile(@RequestBody PatchProfileRequest request) {
        AppUserDetails userDetails = securityContextService.getUserDetails();
        return userService.patchProfile(userDetails, request);
    }

    @GetMapping("/user-info")
    @PreAuthorize("hasAuthority('USER')")
    public ProfileSnapshot userInfo() {
        AppUserDetails userDetails = securityContextService.getUserDetails();
        return userService.getProfileSnapshot(userDetails.getUsername());
    }

    @DeleteMapping("/users/image")
    public DeletePhotoResponse deletePhoto(@RequestBody DeletePhotoRequest photoId) {
        return deletePhotoService.delete(photoId.getId());
    }

    @DeleteMapping("/deactivate/{username}")
    public DeactivateProfile deleteProfile(@PathVariable String username){
        AppUserDetails userDetails = securityContextService.getUserDetails();
        return userService.deactivate(userDetails, username);
    }
}
