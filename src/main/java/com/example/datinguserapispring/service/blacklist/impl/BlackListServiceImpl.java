package com.example.datinguserapispring.service.blacklist.impl;

import com.example.datinguserapispring.dto.admin.AdminActionLogDTO;
import com.example.datinguserapispring.dto.blacklist.request.BlackListedRequest;
import com.example.datinguserapispring.dto.blacklist.response.BlackListUsers;
import com.example.datinguserapispring.dto.blacklist.response.BlackListedResponse;
import com.example.datinguserapispring.dto.fetch.request.FetchRequest;
import com.example.datinguserapispring.dto.photo.response.PhotoDTO;
import com.example.datinguserapispring.exception.Error;
import com.example.datinguserapispring.exception.UserNotFoundException;
import com.example.datinguserapispring.mapper.PhotoMapper;
import com.example.datinguserapispring.model.entity.Admin;
import com.example.datinguserapispring.model.entity.user.Address;
import com.example.datinguserapispring.model.entity.user.BlackList;
import com.example.datinguserapispring.model.entity.user.User;
import com.example.datinguserapispring.model.enums.ActionType;
import com.example.datinguserapispring.repository.AddressRepository;
import com.example.datinguserapispring.repository.AdminRepository;
import com.example.datinguserapispring.repository.BlackListRepository;
import com.example.datinguserapispring.repository.ChatMemberRepository;
import com.example.datinguserapispring.repository.ChatRepository;
import com.example.datinguserapispring.repository.UserRepository;
import com.example.datinguserapispring.service.admin.AdminActionService;
import com.example.datinguserapispring.service.blacklist.BlackListService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@RequiredArgsConstructor
public class BlackListServiceImpl implements BlackListService {

    private final BlackListRepository blackListRepository;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final AddressRepository addressRepository;
    private final ChatRepository chatRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final AdminActionService adminActionService;
    private final PhotoMapper photoMapper;


    @Override
    @Transactional
    public BlackListedResponse setBlackListed(BlackListedRequest request, String appUserDetails) {
        var admin = adminRepository.findByLogin(appUserDetails)
                .orElseThrow(() -> new UserNotFoundException(Error.USER_NOT_FOUND));

        var user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException(Error.USER_NOT_FOUND));

        addToBlackList(admin, user);
        deleteChats(user);

        logAdminAction(admin, user);
        log.info("User with ID '{}' has been added to the blacklist by admin '{}'.", user.getId(), admin.getId());

        return new BlackListedResponse(user.getId());
    }

    @Override
    @Transactional
    public BlackListedResponse removeUserFromBlackList(String userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(Error.USER_NOT_FOUND));

        blackListRepository.deleteBlackListByAppleId(user.getAppleId());
        user.setBlackList(false);

        log.info("User with ID '{}' has been removed from the blacklist.", userId);
        return new BlackListedResponse(userId);
    }

    @Override
    public List<BlackListUsers> getUsers(Pageable pageable) {
        Page<BlackList> all = blackListRepository.findAll(pageable);

        return all.getContent().stream()
                .map(this::convertToBlackListUsers)
                .toList();
    }

    @Override
    public List<BlackListUsers> findBlackListUsers(FetchRequest fetchRequest, int page, int size) {
        Pageable pageable =
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        if (isFilteringRequired(fetchRequest)) {
            return getUsers(pageable);
        }

        Page<BlackList> blackLists = blackListRepository
                .findBlackListsByAdminIdOrAppleId(fetchRequest.getAdminId(), fetchRequest.getAppleId(), pageable);

        return blackLists.stream()
                .map(this::convertToBlackListUsers)
                .toList();
    }

    private void addToBlackList(Admin admin, User user) {
        String appleId = user.getAppleId();
        BlackList blackList = BlackList.builder()
                .admin(admin)
                .blockedUser(user)
                .appleId(appleId)
                .build();
        user.setBlackList(true);
        userRepository.save(user);
        blackListRepository.save(blackList);
    }

    @Async
    public void deleteChats(User user) {
        List<String> chatIds = chatMemberRepository.findAllChatIdsByUserId(user.getId());
        chatRepository.deleteAllByIdIn(chatIds);
    }

    private boolean isFilteringRequired(FetchRequest fetchRequest) {
        return fetchRequest.getAppleId().isEmpty()
                && fetchRequest.getAdminId().isEmpty()
                && fetchRequest.getGender() != null;
    }

    private BlackListUsers convertToBlackListUsers(BlackList blackList) {
        List<PhotoDTO> photoDTOList = blackList.getBlockedUser().getPhotos().stream()
                .map(photoMapper::mapToDTO)
                .toList();
        Address address = addressRepository.getFirstByUserId(blackList.getBlockedUser().getId());
        return BlackListUsers.builder()
                .id(blackList.getBlockedUser().getId())
                .createdAt(blackList.getBlockedUser().getCreatedAt())
                .adminName(blackList.getAdmin().getName())
                .name(blackList.getBlockedUser().getName())
                .gender(blackList.getBlockedUser().getGender().toString())
                .cityName(address.getCity())
                .country(address.getCountry())
                .lookingFor(blackList.getBlockedUser().getLookingFor())
                .appleId(blackList.getAppleId())
                .images(photoDTOList)
                .build();
    }

    private void logAdminAction(Admin admin, User user) {
        adminActionService.saveAdminActionLog(AdminActionLogDTO.builder()
                .user(user)
                .admin(admin)
                .createdAt(LocalDateTime.now())
                .actionType(ActionType.valueOf(ActionType.USER_BLOCK.getValue()))
                .build());

    }
}
