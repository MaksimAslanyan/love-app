package com.example.datinguserapispring.service.bot.impl;


import com.example.datinguserapispring.dto.bot.AgeRangeDTO;
import com.example.datinguserapispring.dto.photo.response.PhotoDataDTO;
import com.example.datinguserapispring.dto.search.request.SearchByCriteria;
import com.example.datinguserapispring.dto.search.response.ProfileSnapshotSearchResponseBot;
import com.example.datinguserapispring.exception.Error;
import com.example.datinguserapispring.exception.UserNotFoundException;
import com.example.datinguserapispring.model.entity.Photo;
import com.example.datinguserapispring.model.entity.bot.Bot2;
import com.example.datinguserapispring.model.entity.bot.BotOnlineStatus;
import com.example.datinguserapispring.model.entity.user.Address;
import com.example.datinguserapispring.model.entity.user.User;
import com.example.datinguserapispring.model.entity.user.UserLike;
import com.example.datinguserapispring.model.enums.Gender;
import com.example.datinguserapispring.repository.*;
import com.example.datinguserapispring.service.bot.SearchBotService;
import com.example.datinguserapispring.service.file.FileDataReaderService;
import com.example.datinguserapispring.service.lookingFor.LookingForService;
import com.example.datinguserapispring.service.photo.PhotoService;
import com.example.datinguserapispring.util.ExtractorAgeFromUlrUtil;
import com.example.datinguserapispring.util.RandomDistanceGeneratorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.datinguserapispring.util.RandomNumberGeneratorUtil.getRandomNumberInt;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchBotServiceImpl implements SearchBotService {

    private static final int ZERO = 0;
    public static final String NAMES_TXT = "names.txt";
    private final Bot2Repository bot2Repository;
    private final AddressRepository addressRepository;
    private final LookingForService lookingForService;
    private final PhotoRepository photoRepository;
    private final FileDataReaderService fileDataReaderService;
    private final UserRepository userRepository;
    private final BlockRepository blockRepository;
    private final UserLikeRepository userLikeRepository;
    private final PhotoService photoService;


    public List<ProfileSnapshotSearchResponseBot> searchBots(SearchByCriteria criteria, String userId) {
        long startTime = System.currentTimeMillis(); // Start time measurement

        log.info("search bots 2 PR: criteria: {}", criteria);

        List<ProfileSnapshotSearchResponseBot> resultList = new ArrayList<>();
        User user = getUserById(userId);
        log.info("search bots 2 PR: user: {}", user);
        Address address = getAddressForUser(user);
        int maxResults = criteria.getMaxResults() - 1;


        ProfileSnapshotSearchResponseBot bot = botLikedUser(userId, address, criteria);
        if (bot != null) {
            resultList.add(bot);
            log.info("search bots 2 PR: Added liked user bot");
        } else {
            log.info("search bots 2 PR: No liked user bot found");
        }

        if (bot == null) {
            maxResults++;
            log.info("search bots 2 PR: Increased maxResults to {}", maxResults);
        }

        List<String> userLookingFor = lookingForService.getUserLookingFor(user.getId());
        log.info("search bots 2 PR: userLookingFor: {}", userLookingFor);

        Map<Integer, List<PhotoDataDTO>> photoList = fileDataReaderService.getPhotoFromPhotosForSearchBots(
                criteria.getAgeFrom(),
                criteria.getAgeTo(),
                Gender.fromValue(user.getTargetGender()),
                maxResults,
                userId);

        List<String> names = fileDataReaderService.getNameFromFile(user, address);


        List<Bot2> bot2s = createRandomBots2(maxResults);
        List<Double> distance = RandomDistanceGeneratorUtil.generate(criteria.getRadius(),
                bot2s.size() - 1, user.isPremium());
        log.info("search bots 2 PR: Found {} bots", bot2s.size());

        List<Integer> photoIndices = new ArrayList<>();
        for (int i = 0; i < photoList.size(); i++) {
            photoIndices.add(i);
        }

        for (Bot2 opponent : bot2s) {
            int randomIndex = photoIndices.get(0);
            photoIndices.remove(0); // Remove the used index to avoid duplicates

            List<PhotoDataDTO> randomPhotoDataList = photoList.get(randomIndex);
            String url = randomPhotoDataList.get(0).getUrl();
            AgeRangeDTO extract = ExtractorAgeFromUlrUtil.extract(url);

            byte minAge = (byte) Math.max(extract.getMinAge(), criteria.getAgeFrom());
            byte maxAge = (byte) Math.min(extract.getMaxAge(), criteria.getAgeTo());
            byte randomAge = 0;
            if (minAge <= maxAge) {
                randomAge = (byte) getRandomNumberInt(minAge, maxAge);
                // Use randomAge within the intersection of age ranges
            }

            double getDistance = distance.get(getRandomNumberInt(ZERO, distance.size() - 1));
            boolean likedMe = false;
            int getRandomName = getRandomNumberInt(ZERO, names.size() - 1);
            String randomBotName = names.get(getRandomName);

            List<String> randomLookingFor = new ArrayList<>(userLookingFor);
            int numRandomLookingFor = Math.min(getRandomNumberInt(1, 3), randomLookingFor.size());
            Collections.shuffle(randomLookingFor);
            randomLookingFor = randomLookingFor.subList(0, numRandomLookingFor);


            ProfileSnapshotSearchResponseBot opponentBot = generateBot(
                    address, randomPhotoDataList,
                    randomAge,
                    null,
                    getDistance, likedMe,
                    randomBotName, randomLookingFor);
            resultList.add(opponentBot);

        }

        Collections.shuffle(resultList);

        long endTime = System.currentTimeMillis(); // End time measurement
        double totalTimeInMs = endTime - startTime;
        double totalTimeInSeconds = totalTimeInMs / 1000.0; // Convert ms to seconds
        log.info("searchBots execution time: {} seconds", totalTimeInSeconds);

        return resultList;
    }

    private ProfileSnapshotSearchResponseBot botLikedUser(String userId, Address address, SearchByCriteria criteria) {
        List<PhotoDataDTO> photoDataDTOS = new ArrayList<>();

        // Log search criteria
        log.info("Searching for user liked by bot with criteria: age range [{} - {}]", criteria.getAgeFrom(), criteria.getAgeTo());

        Optional<UserLike> userLike = userLikeRepository.findUserLikeByUserIdAndBot2AgeRange(
                userId, criteria.getAgeFrom(), criteria.getAgeTo());

        if (userLike.isEmpty()) {
            log.debug("No user like found for userId: {} and criteria: {}", userId, criteria);
            return null;
        }

        Bot2 bot2 = bot2Repository.findById(userLike.get().getBot2().getId()).orElse(null);

        if (bot2 == null) {
            log.warn("Bot2 not found for user like with userId: {}", userId);
            return null;
        }

        List<Photo> photos = photoRepository.findPhotoByBot2Id(bot2.getId());
        List<String> botLookingForList = lookingForService.getBotLookingFor(bot2.getId());
        boolean isBlocked = blockRepository.existsByBlockingUserIdAndBlockedBotId(userId, bot2.getId());

        for (Photo photo : photos) {
            PhotoDataDTO photoDataDTO = new PhotoDataDTO();
            photoDataDTO.setUrl(photo.getUrl());
            photoDataDTO.setIsAvatar(photo.getIsAvatar());
            photoDataDTO.setImage(photoService.getPhoto(photo.getId()));
            photoDataDTOS.add(photoDataDTO);
        }

        ProfileSnapshotSearchResponseBot response = new ProfileSnapshotSearchResponseBot(
                bot2.getId(),
                address.getCity(),
                address.getCountry(),
                bot2.getDistance(),
                bot2.getBotOnlineStatus().isOnline(),
                bot2.getAge(),
                photoDataDTOS,
                botLookingForList,
                true,
                true,
                true,
                false,
                isBlocked,
                true,
                bot2.getNameBot(),
                userLike.get().isBotLiked()
        );

        log.info("Found bot liked by user: {}", userId);
        return response;
    }


    private User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(Error.USER_NOT_FOUND));
    }

    private Address getAddressForUser(User user) {
        return addressRepository.getFirstByUserId(user.getId());
    }

    private ProfileSnapshotSearchResponseBot generateBot(Address address,
                                                         List<PhotoDataDTO> photoList,
                                                         int randomAge,
                                                         BotOnlineStatus botOnlineStatusById,
                                                         double getDistance, boolean likedMe,
                                                         String randomBotName, List<String> randomLookingFor) {
        boolean isOnline = Math.random() > 0.5;

        return ProfileSnapshotSearchResponseBot.builder()
                .userId(UUID.randomUUID().toString())
                .city(address.getCity())
                .country(address.getCountry())
                .distance(getDistance)
                .isOnline(isOnline)
                .age(randomAge)
                .imageLinks(photoList)
                .isLikedMe(true)
                .isActive(true)
                .isLikedMe(likedMe)
                .name(randomBotName)
                .lookingForList(randomLookingFor)
                .isLikedYou(false)
                .build();
    }


    public static List<Bot2> createRandomBots2(int maxResults) {
        List<Bot2> bot2s = new ArrayList<>();
        for (int i = 0; i < maxResults; i++) {
            Bot2 bot2 = new Bot2();
            bot2.setNameBot("randomName");

            bot2s.add(bot2);
        }
        return bot2s;
    }
}
