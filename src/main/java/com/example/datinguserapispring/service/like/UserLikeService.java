package com.example.datinguserapispring.service.like;

import com.example.datinguserapispring.dto.like.request.LikeRequest;
import com.example.datinguserapispring.dto.like.response.LikeResponse;
import com.example.datinguserapispring.dto.like.response.UserLikeResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserLikeService {

    List<UserLikeResponse> getLikeById(String id, Pageable pageable);

    LikeResponse like(LikeRequest request, String id);
}
