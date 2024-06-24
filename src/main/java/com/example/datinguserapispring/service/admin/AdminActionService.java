package com.example.datinguserapispring.service.admin;

import com.example.datinguserapispring.dto.admin.AdminActionFetch;
import com.example.datinguserapispring.dto.admin.AdminActionLogDTO;
import com.example.datinguserapispring.dto.admin.AdminActionLogResponse;
import com.example.datinguserapispring.dto.admin.request.PhotoApproveRequest;
import com.example.datinguserapispring.dto.admin.response.PhotoApproveResponse;
import com.example.datinguserapispring.dto.photo.response.WaitingApprovalPhotoDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminActionService {
    AdminActionLogResponse saveAdminActionLog(AdminActionLogDTO adminActionLogDTO);

    PhotoApproveResponse userPhotoApprove(PhotoApproveRequest request, String adminId);

    List<AdminActionLogResponse> getLastAdminActions(AdminActionFetch request, Pageable pageable);

    List<WaitingApprovalPhotoDTO> getWaitingApprovalPhotos(String login, Pageable pageable);
}
