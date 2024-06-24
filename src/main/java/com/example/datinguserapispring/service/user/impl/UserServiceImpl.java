package com.example.datinguserapispring.service.user.impl;

import com.example.datinguserapispring.dto.fetch.request.FetchRequest;
import com.example.datinguserapispring.dto.photo.response.PhotoDTO;
import com.example.datinguserapispring.dto.user.AddressDTO;
import com.example.datinguserapispring.dto.user.request.PatchProfileRequest;
import com.example.datinguserapispring.dto.user.response.*;
import com.example.datinguserapispring.exception.AuthenticationException;
import com.example.datinguserapispring.exception.Error;
import com.example.datinguserapispring.exception.UserNotFoundException;
import com.example.datinguserapispring.filter.specification.UserSpecification;
import com.example.datinguserapispring.mapper.PhotoMapper;
import com.example.datinguserapispring.mapper.UserMapper;
import com.example.datinguserapispring.model.entity.Photo;
import com.example.datinguserapispring.model.entity.chat.ChatMember;
import com.example.datinguserapispring.model.entity.dictionary.LookingFor;
import com.example.datinguserapispring.model.entity.user.User;
import com.example.datinguserapispring.model.enums.Gender;
import com.example.datinguserapispring.repository.*;
import com.example.datinguserapispring.security.AppUserDetails;
import com.example.datinguserapispring.service.user.UserService;
import com.example.datinguserapispring.util.AgeCalculatorFromDateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PhotoRepository photoRepository;
    private final LookingForRepository lookingForRepository;
    private final AddressRepository addressRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final UserLikeRepository userLikeRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final ChatRepository chatRepository;
    private final AbstractUserServiceHelper abstractUserServiceHelper;
    private final UserMapper userMapper;
    private final PhotoMapper photoMapper;
    private final PremiumPeriodRepository premiumPeriodRepository;


    @Override
    @Transactional
    public ProfileSnapshot patchProfile(AppUserDetails userDetails, PatchProfileRequest request) {
        var userId = userDetails.getUsername();
        log.info("Fetching user by username: {}", userId);

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(Error.USER_NOT_FOUND));

        log.info("Updating user from request");
        updateUserFromRequest(request, user);
        log.info("Saving updated user");

        userRepository.save(user);

        return abstractUserServiceHelper.getProfileSnapshot(user);
    }

    @Override
    public List<ProfileSnapshotForAdminResponse> getAllUsers(FetchRequest fetchRequest, int page, int size) {
        var pageable =
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Specification<User> userSpecification =
                UserSpecification.filterByCriteria(fetchRequest);
        log.info("Fetching users with pageable: {}, specification: {}", pageable, userSpecification);

        Page<User> userList =
                userRepository.findAll(userSpecification, pageable);

        return userList.get().map(abstractUserServiceHelper::converter).toList();
    }

    @Override
    public UserInfoResponse getUserById(String id) {
        log.info("Fetching user by ID: {}", id);
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(Error.USER_NOT_FOUND));

        List<Photo> allByUserId = photoRepository.findAllByUserId(user.getId());
        List<PhotoDTO> photoUrls = convertToPhotoDTOList(allByUserId);

        return abstractUserServiceHelper.buildUserInfoResponse(user, photoUrls);
    }

    @Override
    @Transactional
    public DeactivateProfile deactivate(AppUserDetails userDetails, String username) {
        String id = userDetails.getUsername();
        var user = userRepository.findById(id)
                .orElseThrow(() -> new AuthenticationException(Error.USER_NOT_FOUND));

        resetUserAttributes(user);
        deleteRelatedEntities(id);

        return new DeactivateProfile(user.getAppleId());

    }

    @Override
    public ProfileSnapshot getProfileSnapshot(String id) {
        log.info("Fetching user by ID: {}", id);
        User firstByUserName = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(Error.USER_NOT_FOUND));

        ProfileSnapshot profileSnapshot =
                userMapper.mapToProfileSnapshot(firstByUserName);
        List<Photo> allByUserId =
                photoRepository.findAllByUserId(firstByUserName.getId());

        List<PhotoDTO> photoUrls = allByUserId.stream()
                .map(photoMapper::mapToDTO)
                .toList();
        profileSnapshot.setImages(photoUrls);

        return abstractUserServiceHelper.buildUserProfileSnapshotResponse(firstByUserName, photoUrls);
    }

    @Override
    public UsersCountResponse getUsersCount() {
        log.info("Counting users by gender");
        long maleCount = userRepository.countUserByGenderAndIsBlackListIsFalse(Gender.MALE);
        long femaleCount = userRepository.countUserByGenderAndIsBlackListIsFalse(Gender.FEMALE);
        log.info("Creating UsersCountResponse");

        return new UsersCountResponse(maleCount + femaleCount, maleCount, femaleCount);
    }

    private void updateUserFromRequest(PatchProfileRequest request, User user) {
        if (request.getName() != null) {
            user.setName(request.getName());
        }

        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }

        if (request.getTargetGenders() != null) {
            user.setTargetGender(String.join(",", request.getTargetGenders()));
        }

        if (request.getDob() != null) {
            user.setDob(request.getDob());
            user.setAge(AgeCalculatorFromDateUtil.calculate(request.getDob()));
        }

        if (request.getLookingFor() != null) {
            List<String> lookingFor = request.getLookingFor().getLookingForList();

            saveNewLookingForIfNotExists(user, lookingFor);
        }

    }

    private void resetUserAttributes(User user) {
        user.setGender(Gender.NOT_CHOSEN);
        user.setTargetGender(null);
        user.setName(null);
        user.setActive(false);
        user.setPremium(false);
    }

    private void saveNewLookingForIfNotExists(User user, List<String> lookingFor) {
        // Retrieve existing LookingFor entries for the user
        List<LookingFor> existingLookingForList = lookingForRepository.findAllByUserId(user.getId());

        // Delete all existing entries for the user in batches
        lookingForRepository.deleteInBatch(existingLookingForList);

        // Create and save new LookingFor entries
        List<LookingFor> newLookingForList = lookingFor.stream()
                .map(lookingForValue -> new LookingFor(lookingForValue, user, LocalDateTime.now()))
                .toList();

        lookingForRepository.saveAll(newLookingForList);
    }

    private List<PhotoDTO> convertToPhotoDTOList(List<Photo> photos) {
        return photos.stream()
                .map(photoMapper::mapToDTO)
                .toList();
    }

    private void deleteRelatedEntities(String userId) {
        lookingForRepository.deleteAllByUserId(userId);
        photoRepository.deleteAllByUserId(userId);

        locationRepository.deleteAllByUserId(userId);
        userLikeRepository.deleteAllByOpponentId(userId);
        userLikeRepository.deleteAllByUserId(userId);


        List<ChatMember> chatMembers = chatMemberRepository.findChatMemberByUserId(userId);
        List<String> chatIds = chatMembers.stream()
            .map(chatMember -> chatMember.getChat().getId())
            .toList();

        if (!chatIds.isEmpty()) {
            chatRepository.deleteAllById(chatIds);
        }

        premiumPeriodRepository.deleteAllByUserId(userId);
    }
}
