package com.example.datinguserapispring.api.controller.admin;


import com.example.datinguserapispring.dto.admin.AdminActionFetch;
import com.example.datinguserapispring.dto.admin.AdminActionLogResponse;
import com.example.datinguserapispring.dto.admin.request.PhotoApproveRequest;
import com.example.datinguserapispring.dto.admin.response.PhotoApproveResponse;
import com.example.datinguserapispring.dto.photo.response.WaitingApprovalPhotoDTO;
import com.example.datinguserapispring.security.SecurityContextService;
import com.example.datinguserapispring.service.admin.AdminActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminActionController {

    private final AdminActionService adminActionService;
    private final SecurityContextService securityContextService;


    @PostMapping("/user-images/approve")
    public PhotoApproveResponse userPhotoApprove(@RequestBody PhotoApproveRequest request) {
        String login = securityContextService.getUserDetails().getUsername();
        return adminActionService.userPhotoApprove(request, login);
    }

    @GetMapping("/user-images/waiting-approval")
    public List<WaitingApprovalPhotoDTO> waitingApprovalPhotoDTOS(Pageable pageable) {
        String login = securityContextService.getUserDetails().getUsername();
        return adminActionService.getWaitingApprovalPhotos(login, pageable);
    }

    @PostMapping("/actions")
    public List<AdminActionLogResponse> findLastAdminActions(@RequestBody AdminActionFetch request,
                                                             Pageable pageable) {
        return adminActionService.getLastAdminActions(request, pageable);
    }
}
