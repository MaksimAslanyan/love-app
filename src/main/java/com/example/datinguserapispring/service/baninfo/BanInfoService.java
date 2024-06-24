package com.example.datinguserapispring.service.baninfo;

import com.example.datinguserapispring.dto.ban.request.BanInfoRequest;
import com.example.datinguserapispring.dto.ban.response.CreateBanResponse;
import com.example.datinguserapispring.dto.ban.response.DeleteBanResponse;
import com.example.datinguserapispring.dto.ban.response.GetBanInfoResponse;

public interface BanInfoService {
    CreateBanResponse banUser(String id, BanInfoRequest request);

    GetBanInfoResponse getByUserId(String id);

    DeleteBanResponse deleteBan(String id);
}
