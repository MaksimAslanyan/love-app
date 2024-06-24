package com.example.datinguserapispring.service.user.impl;

import com.example.datinguserapispring.dto.photo.response.PhotoDTO;
import com.example.datinguserapispring.dto.user.response.ProfileSnapshot;
import com.example.datinguserapispring.dto.user.response.ProfileSnapshotForAdminResponse;
import com.example.datinguserapispring.dto.user.response.UserInfoResponse;
import com.example.datinguserapispring.mapper.PhotoMapper;
import com.example.datinguserapispring.mapper.UserMapper;
import com.example.datinguserapispring.model.entity.Photo;
import com.example.datinguserapispring.model.entity.user.Address;
import com.example.datinguserapispring.model.entity.user.User;
import com.example.datinguserapispring.repository.AddressRepository;
import com.example.datinguserapispring.repository.BlackListRepository;
import com.example.datinguserapispring.repository.PhotoRepository;
import com.example.datinguserapispring.repository.PremiumPeriodRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
class UserServiceHelperImpl extends AbstractUserServiceHelper {

  private final AddressRepository addressRepository;
  private final PhotoRepository photoRepository;
  private final BlackListRepository blackListRepository;
  private final PremiumPeriodRepository premiumPeriodRepository;
  private final PhotoMapper photoMapper;
  private final UserMapper userMapper;

  @Override
  UserInfoResponse buildUserInfoResponse(User user, List<PhotoDTO> photoUrls) {
    List<Address> addresses = addressRepository.findAllByUser(user);
    String lastCityName =
        addresses.isEmpty() ? null : addresses.get(addresses.size() - 1).getCity();
    String lastCountry =
        addresses.isEmpty() ? null : addresses.get(addresses.size() - 1).getCountry();
    return UserInfoResponse.builder()
        .id(user.getId())
        .targetGender(user.getTargetGender())
        .cityName(lastCityName)
        .country(lastCountry)
        .age(user.getAge())
        .roles(user.getRoles())
        .dob(user.getDob())
        .name(user.getName())
        .appleId(user.getAppleId())
        .lookingFor(user.getLookingFor())
        .gender(user.getGender())
        .images(photoUrls)
        .isBlocked(user.isBlackList())
        .build();
  }

  @Override
  ProfileSnapshotForAdminResponse converter(User user) {
    List<PhotoDTO> photoDTOList = user.getPhotos().stream()
        .map(photoMapper::mapToDTO)
        .toList();
    Optional<Address> addressOptional = Optional
        .ofNullable(addressRepository.getFirstByUserId(user.getId()));
    if (addressOptional.isEmpty()){
      Address address = new Address(user,"Moscow","Russia");
      addressRepository.save(address);
    }
    return ProfileSnapshotForAdminResponse.builder()
        .id(user.getId())
        .createdAt(user.getCreatedAt())
        .name(user.getName())
        .gender(user.getGender().toString())
        .cityName(addressOptional.map(Address::getCity)
            .orElse("Moscow"))
        .country(addressOptional.map(Address::getCountry)
            .orElse("Russia"))
        .lookingFor(user.getLookingFor())
        .appleId(user.getAppleId())
        .images(photoDTOList)
        .build();
  }

  @Override
  ProfileSnapshot getProfileSnapshot(User user) {
    List<Photo> allByUserId = photoRepository.findAllByUserId(user.getId());

    ProfileSnapshot profileSnapshot = userMapper.mapToProfileSnapshot(user);

    List<PhotoDTO> photoUrls = allByUserId.stream()
        .map(photoMapper::mapToDTO)
        .toList();

    profileSnapshot.setPreviouslyHadSubscription(hadPremiumSubscription(user));
    profileSnapshot.setExistFirebaseToken(existFirebaseToken(user));
    profileSnapshot.setImages(photoUrls);
    if (user.getAddresses() != null && !user.getAddresses().isEmpty()) {
      Address lastAddress = user.getAddresses().get(user.getAddresses().size() - 1);
      profileSnapshot.setCountry(lastAddress.getCountry());
      profileSnapshot.setCity(lastAddress.getCity());
    }
    return profileSnapshot;
  }

  @Override
  ProfileSnapshot buildUserProfileSnapshotResponse(User user, List<PhotoDTO> photoUrls) {
    List<Address> addresses = addressRepository.findAllByUser(user);
    var isInBlackList = blackListRepository.existsByAppleId(user.getAppleId());
    var existFirebaseToken = existFirebaseToken(user);
    var hasPremiumSubscription = hadPremiumSubscription(user);

    var lastCityName =
        addresses.isEmpty() ? null : addresses.get(addresses.size() - 1).getCity();
    var lastCountry =
        addresses.isEmpty() ? null : addresses.get(addresses.size() - 1).getCountry();
    return ProfileSnapshot.builder()
        .id(user.getId())
        .city(lastCityName)
        .country(lastCountry)
        .age(user.getAge())
        .name(user.getName())
        .lookingFor(user.getLookingFor())
        .gender(user.getGender().name())
        .images(photoUrls)
        .premium(user.isPremium())
        .isBlackList(isInBlackList)
        .existFirebaseToken(existFirebaseToken)
        .previouslyHadSubscription(hasPremiumSubscription)
        .build();
  }

  private boolean hadPremiumSubscription(User user) {
    return premiumPeriodRepository.existsByUserId(user.getId());
  }

  private  boolean existFirebaseToken(User user) {
    return user.getToken() != null;
  }
}
