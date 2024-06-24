package com.example.datinguserapispring.service.search.impl;

import com.example.datinguserapispring.dto.user.response.ProfileSnapshotSearchResponse;
import com.example.datinguserapispring.dto.location.LatLonDTO;
import com.example.datinguserapispring.dto.photo.response.PhotoDTO;
import com.example.datinguserapispring.dto.search.UserDistanceAssignDTO;
import com.example.datinguserapispring.dto.search.request.SearchByCriteria;
import com.example.datinguserapispring.mapper.LocationMapper;
import com.example.datinguserapispring.model.entity.Location;
import com.example.datinguserapispring.model.entity.user.Address;
import com.example.datinguserapispring.model.entity.user.User;
import com.example.datinguserapispring.repository.*;
import com.example.datinguserapispring.security.SecurityContextService;
import com.example.datinguserapispring.service.search.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private static final List<ProfileSnapshotSearchResponse> metersToKmList = new ArrayList<>();
    private static final List<ProfileSnapshotSearchResponse> km1To3List = new ArrayList<>();
    private static final List<ProfileSnapshotSearchResponse> km3To5List = new ArrayList<>();
    private static final List<ProfileSnapshotSearchResponse> km5To10List = new ArrayList<>();
    private static final List<ProfileSnapshotSearchResponse> km10To15List = new ArrayList<>();
    private static final List<ProfileSnapshotSearchResponse> km15To30List = new ArrayList<>();
    private static final List<ProfileSnapshotSearchResponse> km30ToAwayList = new ArrayList<>();

    private final PhotoRepository photoRepository;
    private final SearchRepository searchRepository;
    private final LocationRepository locationRepository;
    private final UserLikeRepository userLikeRepository;
    private final AddressRepository addressRepository;
    private final SearchServiceHelper searchServiceHelper;
    private final LocationMapper locationMapper;
    private final SecurityContextService securityContextService;


    @Override
    public List<ProfileSnapshotSearchResponse> search(SearchByCriteria criteria) {
        String userId = securityContextService.getUserDetails().getUsername();

        List<Location> myPlace = locationRepository.findLocationByUserId(userId);
        LatLonDTO myPlaceDTO = locationMapper.mapToDTO(myPlace.get(0));
        List<User> users = searchRepository.searchUserByCriteria(myPlaceDTO, criteria);

        List<ProfileSnapshotSearchResponse> resultList = new ArrayList<>();
        for (User opponent : users) {
            List<PhotoDTO> photoUrls = Collections.emptyList();

            boolean likedMe = userLikeRepository.existsUserLikeByUserIdAndOpponentId(userId, opponent.getId());
            double opponentDistance = searchServiceHelper.getOpponentDistance(myPlaceDTO, opponent);
            Address address = addressRepository.getFirstByUserId(opponent.getId());

            createUserDistanceAssignDTO(
                    opponent, photoUrls,
                    likedMe, opponentDistance, address);

            assignUserToDistanceRange(createUserDistanceAssignDTO(
                    opponent, photoUrls,
                    likedMe, opponentDistance, address));

            resultList.add(getProfileSnapshotSearchResponse(createUserDistanceAssignDTO(
                    opponent, photoUrls,
                    likedMe, opponentDistance, address)));
        }

        return getUsersByProbabilities(resultList.size());
    }


    private List<ProfileSnapshotSearchResponse> getUsersByProbabilities(int totalUsers) {
        List<ProfileSnapshotSearchResponse> selectedUsers = new ArrayList<>();

        double totalValue = 100.0; // Total value in the equation

        // Solve the equation: 15x + 30x = 100
        double x = totalValue / (15 + 30 + 25 + 15 + 10); // Calculate the value of x


        double range1Value = 15 * x; // Value for the first range (15x)
        double range2Value = 30 * x; // Value for the second range (30x)
        double range3Value = 25 * x; // Value for the second range (5x)
        double range4Value = 15 * x; // Value for the second range (5x)
        double range5Value = 10 * x; // Value for the second range (5x)

        // Calculate the number of users to select for each distance range
        int km3To5Count = (int) (totalUsers * 0.5);     // 50% of totalUsers
        int km5To10Count = (int) (totalUsers * 0.3);    // 30% of totalUsers
        int km10To15Count = (int) (totalUsers * 0.15);  // 15% of totalUsers
        int km15To30Count = (int) (totalUsers * 0.05);  // 5% of totalUsers

        // Select users from each distance range
        selectUsersFromDistanceRange(km3To5List, km3To5Count, selectedUsers);
        selectUsersFromDistanceRange(km5To10List, km5To10Count, selectedUsers);
        selectUsersFromDistanceRange(km10To15List, km10To15Count, selectedUsers);
        selectUsersFromDistanceRange(km15To30List, km15To30Count, selectedUsers);

        return selectedUsers;
    }

    private void selectUsersFromDistanceRange(List<ProfileSnapshotSearchResponse> distanceRangeList, int count,
                                              List<ProfileSnapshotSearchResponse> selectedUsers) {
        shuffleList(distanceRangeList);
        int size = Math.min(distanceRangeList.size(), count);
        selectedUsers.addAll(distanceRangeList.subList(0, size));
    }

    private static UserDistanceAssignDTO createUserDistanceAssignDTO(User opponent,
                                                                     List<PhotoDTO> photoUrls,
                                                                     boolean likedMe,
                                                                     double opponentDistance,
                                                                     Address address) {
        return UserDistanceAssignDTO.builder()
                .user(opponent)
                .city(address.getCity())
                .country(address.getCountry())
                .images(photoUrls)
                .opponentDistance(opponentDistance)
                .likedMe(likedMe)
                .build();
    }

    private void shuffleList(List<ProfileSnapshotSearchResponse> userList) {
        Collections.shuffle(userList);
    }

    public void assignUserToDistanceRange(UserDistanceAssignDTO dto) {
        ProfileSnapshotSearchResponse profileSnapshot =
                getProfileSnapshotSearchResponse(dto);

        if (dto.getOpponentDistance() >= 0.0 && dto.getOpponentDistance() < 1.0) {
            metersToKmList.add(profileSnapshot);
        }
        if (dto.getOpponentDistance() >= 1.0 && dto.getOpponentDistance() < 3.0) {
            km1To3List.add(profileSnapshot);
        }
        if (dto.getOpponentDistance() >= 3.0 && dto.getOpponentDistance() < 5.0) {
            km3To5List.add(profileSnapshot);
        }
        if (dto.getOpponentDistance() >= 5.0 && dto.getOpponentDistance() < 10.0) {
            km5To10List.add(profileSnapshot);
        }
        if (dto.getOpponentDistance() >= 10.0 && dto.getOpponentDistance() < 15.0) {
            km10To15List.add(profileSnapshot);
        }
        if (dto.getOpponentDistance() >= 15.0 && dto.getOpponentDistance() <= 30.0) {
            km15To30List.add(profileSnapshot);
        }
        if (dto.getOpponentDistance() >= 30.0 && dto.getOpponentDistance() < Integer.MAX_VALUE) {
            km30ToAwayList.add(profileSnapshot);
        }
    }

    private  ProfileSnapshotSearchResponse getProfileSnapshotSearchResponse(UserDistanceAssignDTO dto) {
        return ProfileSnapshotSearchResponse.builder()
                .userId(dto.getUser().getId())
                .city(dto.getCity())
                .country(dto.getCountry())
                .distance(dto.getOpponentDistance())
                .age(dto.getUser().getAge())
                .imageLinks(dto.getImages())
                .isLikedMe(dto.isLikedMe())
                .name(dto.getUser().getName())
                .isOnline(dto.getUser().getOnlineStatus().isOnline())
                .isPremium(dto.getUser().isPremium())
                .build();
    }

}



