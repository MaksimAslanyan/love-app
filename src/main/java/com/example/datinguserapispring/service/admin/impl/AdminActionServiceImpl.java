package com.example.datinguserapispring.service.admin.impl;

import com.example.datinguserapispring.constants.AdminRoles;
import com.example.datinguserapispring.constants.UserType;
import com.example.datinguserapispring.dto.admin.AdminActionFetch;
import com.example.datinguserapispring.dto.admin.AdminActionLogDTO;
import com.example.datinguserapispring.dto.admin.AdminActionLogResponse;
import com.example.datinguserapispring.dto.admin.request.PhotoApproveRequest;
import com.example.datinguserapispring.dto.admin.response.PhotoApproveResponse;
import com.example.datinguserapispring.dto.photo.response.WaitingApprovalPhotoDTO;
import com.example.datinguserapispring.exception.Error;
import com.example.datinguserapispring.exception.UserNotFoundException;
import com.example.datinguserapispring.filter.specification.AdminActionSpecification;
import com.example.datinguserapispring.mapper.AdminActionMapper;
import com.example.datinguserapispring.mapper.PhotoMapper;
import com.example.datinguserapispring.model.entity.Admin;
import com.example.datinguserapispring.model.entity.Photo;
import com.example.datinguserapispring.model.entity.user.AdminAction;
import com.example.datinguserapispring.model.entity.user.User;
import com.example.datinguserapispring.model.enums.ActionType;
import com.example.datinguserapispring.model.enums.PhotoState;
import com.example.datinguserapispring.repository.AdminActionRepository;
import com.example.datinguserapispring.repository.AdminRepository;
import com.example.datinguserapispring.repository.PhotoRepository;
import com.example.datinguserapispring.repository.UserRepository;
import com.example.datinguserapispring.service.admin.AdminActionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminActionServiceImpl implements AdminActionService {

    private final AdminActionRepository adminActionRepository;
    private final PhotoRepository photoRepository;
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final AdminActionMapper adminActionMapper;
    private final PhotoMapper photoMapper;


    @Override
    public AdminActionLogResponse saveAdminActionLog(AdminActionLogDTO adminActionLogDTO) {
        adminActionRepository.save(adminActionMapper.mapToEntity(adminActionLogDTO));
        return new AdminActionLogResponse(adminActionLogDTO.getUser().getId());
    }

    @Override
    public List<AdminActionLogResponse> getLastAdminActions(AdminActionFetch adminActionFetch, Pageable pageable) {
        var page =
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        Sort.by(Sort.Direction.DESC, "createdAt"));

        var adminActionSpecification =
                AdminActionSpecification.filterByCriteria(adminActionFetch);

        var userList =
                adminActionRepository.findAll(adminActionSpecification, page);

        return userList.stream()
                .map(this::mapAdminActionToLogResponse)
                .toList();
    }

    @Override
    public List<WaitingApprovalPhotoDTO> getWaitingApprovalPhotos(String login, Pageable pageable) {
        var page =
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        Sort.by(Sort.Direction.DESC, "createdAt"));
        var photos = photoRepository
                .findAllByApproveIsFalseAndParentTypeEquals(UserType.USER, page);

        return photos.stream()
                .map(photoMapper::mapToWaitingApprovalPhotoDTO)
                .toList();
    }

    @Override
    public PhotoApproveResponse userPhotoApprove(PhotoApproveRequest request, String login) {
        var admin = adminRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(Error.USER_NOT_FOUND));

        var photo = photoRepository.findById(request.getImageId())
                .orElseThrow(() -> new UserNotFoundException(Error.USER_NOT_FOUND));

        var user = userRepository.findById(photo.getUser().getId())
                .orElseThrow(() -> new UserNotFoundException(Error.USER_NOT_FOUND));

        if (request.isApprove()) {
            approvePhoto(photo, admin, user);
        }

        if (!request.isApprove()) {
            rejectPhoto(photo, admin, user);
        }

        photoRepository.save(photo);

        return new PhotoApproveResponse(admin.getId());
    }

    private void approvePhoto(Photo photo, Admin admin, User user) {
        photo.setApprove(true);
        photo.setPhotoState(PhotoState.APPROVE);
        logAdminAction(admin, user, ActionType.PHOTO_APPROVE);
    }

    private void rejectPhoto(Photo photo, Admin admin, User user) {
        photo.setApprove(false);
        photo.setPhotoState(PhotoState.REJECT);
        logAdminAction(admin, user, ActionType.PHOTO_REJECT);
    }

    private AdminActionLogResponse mapAdminActionToLogResponse(AdminAction adminAction) {
        return AdminActionLogResponse.builder()
                .userId(adminAction.getUser().getId())
                .appleId(adminAction.getUser().getAppleId())
                .imageId(adminAction.getImageId())
                .adminName(adminAction.getAdmin().getName())
                .adminRole(AdminRoles.getAdminRole(adminAction.getAdmin().getRole()))
                .createdAt(adminAction.getCreatedAt())
                .actionType(adminAction.getActionType())
                .build();
    }

    private void logAdminAction(Admin admin, User user, ActionType actionType) {
        saveAdminActionLog(AdminActionLogDTO.builder()
                .user(user)
                .admin(admin)
                .createdAt(LocalDateTime.now())
                .actionType(ActionType.valueOf(actionType.getValue()))
                .build());
    }
}