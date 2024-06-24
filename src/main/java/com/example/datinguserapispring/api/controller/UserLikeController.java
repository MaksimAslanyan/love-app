package com.example.datinguserapispring.api.controller;


import com.example.datinguserapispring.dto.like.request.LikeRequest;
import com.example.datinguserapispring.dto.like.response.LikeResponse;
import com.example.datinguserapispring.dto.like.response.UserLikeResponse;
import com.example.datinguserapispring.security.AppUserDetails;
import com.example.datinguserapispring.security.SecurityContextService;
import com.example.datinguserapispring.service.like.UserLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserLikeController {

    private final UserLikeService userLikeService;
    private final SecurityContextService securityContextService;


    @PostMapping("/like")
    public LikeResponse like(@RequestBody LikeRequest request){
        String id = securityContextService.getUserDetails().getUsername();
        return userLikeService.like(request, id);
    }

    @GetMapping("/likes")
    public List<UserLikeResponse> getUserLike(Pageable pageable){
        AppUserDetails userDetails = securityContextService.getUserDetails();
        return userLikeService.getLikeById(userDetails.getUsername(),pageable);
    }
}
