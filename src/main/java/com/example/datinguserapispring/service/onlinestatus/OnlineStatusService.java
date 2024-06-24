package com.example.datinguserapispring.service.onlinestatus;

import com.example.datinguserapispring.dto.onlinestatus.request.OnlineStatusRequest;
import com.example.datinguserapispring.dto.onlinestatus.response.OnlineStatusResponse;

public interface OnlineStatusService {

    OnlineStatusResponse onlineStatusActivityForUser(OnlineStatusRequest request);
    OnlineStatusResponse onlineStatusActivityForAdmin(OnlineStatusRequest request);
}
