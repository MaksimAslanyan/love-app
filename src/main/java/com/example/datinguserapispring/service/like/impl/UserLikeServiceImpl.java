package com.example.datinguserapispring.service.like.impl;

import com.example.datinguserapispring.dto.chat.request.ChatCreateRequest;
import com.example.datinguserapispring.dto.like.request.LikeRequest;
import com.example.datinguserapispring.dto.like.response.LikeResponse;
import com.example.datinguserapispring.dto.like.response.UserLikeResponse;
import com.example.datinguserapispring.dto.photo.response.PhotoDTO;
import com.example.datinguserapispring.exception.Error;
import com.example.datinguserapispring.exception.UserNotFoundException;
import com.example.datinguserapispring.mapper.PhotoMapper;
import com.example.datinguserapispring.model.entity.Notification;
import com.example.datinguserapispring.model.entity.Photo;
import com.example.datinguserapispring.model.entity.bot.Bot2;
import com.example.datinguserapispring.model.entity.user.Address;
import com.example.datinguserapispring.model.entity.user.Block;
import com.example.datinguserapispring.model.entity.user.LikeRecord;
import com.example.datinguserapispring.model.entity.user.User;
import com.example.datinguserapispring.model.entity.user.UserLike;
import com.example.datinguserapispring.projection.LikeResponseProjection;
import com.example.datinguserapispring.projection.LookingForProjection;
import com.example.datinguserapispring.repository.AddressRepository;
import com.example.datinguserapispring.repository.BlockRepository;
import com.example.datinguserapispring.repository.BotOnlineStatusRepository;
import com.example.datinguserapispring.repository.LikeRecordRepository;
import com.example.datinguserapispring.repository.LookingForRepository;
import com.example.datinguserapispring.repository.NotificationRepository;
import com.example.datinguserapispring.repository.PhotoRepository;
import com.example.datinguserapispring.repository.UserLikeRepository;
import com.example.datinguserapispring.repository.UserRepository;
import com.example.datinguserapispring.service.bot.GenerateBotService;
import com.example.datinguserapispring.service.chat.ChatStartService;
import com.example.datinguserapispring.service.like.UserLikeService;
import lombok.RequiredArgsConstructor;
import com.example.datinguserapispring.model.entity.dictionary.LookingFor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserLikeServiceImpl implements UserLikeService {

    private static final double LIKED_BACK_PROBABILITY = 0.25;
    private static final int USER_LIKE_LIMIT = 15;
    private static final String USER_LIKE_LIMIT_MESSAGE = "User has reached likes limit";

    private final UserLikeRepository userLikeRepository;
    private final AddressRepository addressRepository;
    private final LookingForRepository lookingForRepository;
    private final UserRepository userRepository;
    private final BlockRepository blockRepository;
    private final PhotoMapper photoMapper;
    private final PhotoRepository photoRepository;
    private final ChatStartService chatStartService;
    private final GenerateBotService generateBotService;
    private final NotificationRepository notificationRepository;
    private final LikeRecordRepository likeRecordRepository;

    private static final List<TimeInterval> timeIntervals = new ArrayList<>();

    static {
        timeIntervals.add(new TimeInterval(Duration.ofMinutes(5), Duration.ofMinutes(30), 15));
        timeIntervals.add(new TimeInterval(Duration.ofMinutes(30), Duration.ofHours(3), 15));
        timeIntervals.add(new TimeInterval(Duration.ofHours(3), Duration.ofHours(6), 25));
        timeIntervals.add(new TimeInterval(Duration.ofHours(6), Duration.ofHours(9), 10));
        timeIntervals.add(new TimeInterval(Duration.ofHours(9), Duration.ofHours(12), 10));
        timeIntervals.add(new TimeInterval(Duration.ofHours(12), Duration.ofHours(18), 10));
        timeIntervals.add(new TimeInterval(Duration.ofHours(18), Duration.ofHours(24), 5));
        timeIntervals.add(new TimeInterval(Duration.ofHours(24), Duration.ofHours(36), 4));
        timeIntervals.add(new TimeInterval(Duration.ofHours(36), Duration.ofHours(48), 3));
        timeIntervals.add(new TimeInterval(Duration.ofHours(48), Duration.ofHours(72), 2));
        timeIntervals.add(new TimeInterval(Duration.ofHours(72), Duration.ofHours(120), 1));
    }


    @Override
    @Transactional
    public LikeResponse like(LikeRequest request, String id) {
        log.info("Start processing 'like' request.");
        String bot2Id = request.getId();
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(Error.USER_NOT_FOUND));

        var date = LocalDateTime.now().minusHours(24);
        var isLikeExists = userLikeRepository.existsUserLikeByUserIdAndBot2Id(id, bot2Id);


        if (hasExceededLikeDayLimit(user)) {
            return new LikeResponse(USER_LIKE_LIMIT_MESSAGE);
        }

        if (hasExceededUserLikeLimit(user, date)) {
            return new LikeResponse(USER_LIKE_LIMIT_MESSAGE);
        }

        if (isLikeExists) {
            var chatRequest = new ChatCreateRequest(user.getId(), bot2Id);
            var userLike = userLikeRepository.findUserLikeByUserIdAndBot2Id(id, bot2Id);
            var youLiked = true;
            userLike.setBotLiked(youLiked);
            userLike.setHasMatchChat(true);
            chatStartService.createChatWhenUserLike(chatRequest);
        }

        if (!isLikeExists) {
            handleMatchedLikeEvent(request, id, user);
        }

        likeRecordSave(user);

        return new LikeResponse(user.getId());
    }

    @Async
    public void likeRecordSave(User user) {
        LikeRecord likeRecord = LikeRecord.builder()
                .likedAt(LocalDateTime.now())
                .user(user)
                .build();

        likeRecordRepository.save(likeRecord);
    }

    @Override
    public List<UserLikeResponse> getLikeById(String id, Pageable pageable) {
        long startTime = System.currentTimeMillis();
        log.info("Fetching user likes for id: {}", id);
        Address address = addressRepository.findTop1ByUserId(id);

        List<UserLikeResponse> userLikeResponseList = new ArrayList<>();

        Page<LikeResponseProjection> userLikes = userLikeRepository
                .findByUserIdAndCreatedAtBefore(id, LocalDateTime.now(), pageable);

        Set<String> bot2Ids = userLikes.stream()
                .map(LikeResponseProjection::getBotId)
                .collect(Collectors.toSet());

        Map<String, List<PhotoDTO>> photoMap = photoRepository
                .mapToPhotoDTO(photoRepository.findPhotoByBot2IdIn(bot2Ids));

        Map<String, List<String>> lookingForsMap =
                lookingForRepository.findLookingForByBot2IdIn(bot2Ids)
                        .stream()
                        .collect(Collectors.groupingBy(
                                LookingForProjection::getBot2Id,
                                Collectors.flatMapping(
                                        lookingForProjection -> Arrays.stream(lookingForProjection.getLookingFor().split(",")),
                                        Collectors.toList()
                                )
                        ));

        for (LikeResponseProjection userLike : userLikes) {
            Bot2 bot2 = new Bot2(userLike.getBotId(), userLike.getNameBot(), userLike.getAge(), userLike.getDistance());

            Block block = blockRepository.findByBlockingUserIdAndBlockedBotId(id, bot2.getId());
            boolean isBlocked = block != null && block.isBlocked();
            List<PhotoDTO> photos = photoMap.get(bot2.getId());
            List<String> lookingForList = lookingForsMap.get(bot2.getId());

            if (photos == null || lookingForList == null) {
                continue;
            }

            UserLikeResponse userLikeResponse = getUserLikeResponse(
                    address, bot2,
                    userLike, photos,
                    isBlocked, lookingForList);

            userLikeResponseList.add(userLikeResponse);
        }
        long endTime = System.currentTimeMillis();
        log.info("Execution time: {} seconds", (endTime - startTime) / 1000.0);

        return userLikeResponseList;
    }

    private void handleMatchedLikeEvent(LikeRequest request, String id, User user) {
        Bot2 bot2 = generateBotService.generate(request, id);

        Random random = new Random();
        boolean likedBack = random.nextDouble() <= LIKED_BACK_PROBABILITY;

        if (likedBack) {
            TimeInterval randomInterval = selectRandomInterval(random);
            LocalDateTime likeTime = LocalDateTime.now().plus(randomInterval.getDuration());

            UserLike newLike = createUserLike(user, bot2, likeTime);

            Notification notification = createNotification(user, bot2, likeTime);

            notificationRepository.save(notification);
            var chatRequest = new ChatCreateRequest(user.getId(), bot2.getId());
            chatStartService.createChatWhenLikeBack(chatRequest, likeTime);
            log.info("Saving new like: {}", newLike);
            userLikeRepository.save(newLike);
            log.info("Like saved successfully.");
        }
    }

    private UserLikeResponse getUserLikeResponse(Address address, Bot2 bot2,
                                                 LikeResponseProjection userLike, List<PhotoDTO> photos,
                                                 boolean isBlocked, List<String> lookingForList) {
        return UserLikeResponse.builder()
                .userId(bot2.getId())
                .name(bot2.getNameBot())
                .age(bot2.getAge())
                .isVerified(true)
                .distance(bot2.getDistance())
                .city(address.getCity())
                .country(address.getCountry())
                .isOnline(userLike.getOnline() != null ? userLike.getOnline() : false)
                .imageLinks(photos)
                .isLikedMe(true)
                .isLikedYou(userLike.getIsLikedYou())
                .isActive(true)
                .isPremium(true)
                .isBlocked(isBlocked)
                .lookingForList(lookingForList)
                .isBlockedMe(false)
                .build();
    }

    private UserLike createUserLike(User user, Bot2 bot2, LocalDateTime likeTime) {
        return UserLike.builder()
                .bot2(bot2)
                .user(user)
                .userAuthorType("USER")
                .isLikedYou(true)
                .isBotLiked(true)
                .createdAt(likeTime)
                .build();
    }

    private Notification createNotification(User user, Bot2 bot2, LocalDateTime likeTime) {
        return Notification.builder()
                .user(user)
                .opponentId(bot2.getId())
                .userType("BOT")
                .sendTime(likeTime)
                .isSend(false)
                .build();
    }

    private List<PhotoDTO> convertToPhotoDTOList(List<Photo> photos) {
        return photos.stream()
                .map(photoMapper::mapToDTO)
                .toList();
    }

    private TimeInterval selectRandomInterval(Random random) {
        int totalProbability = timeIntervals.stream()
                .mapToInt(TimeInterval::getProbability)
                .sum();
        int randomProbability = random.nextInt(totalProbability) + 1;
        int cumulativeProbability = 0;

        for (TimeInterval interval : timeIntervals) {
            cumulativeProbability += interval.getProbability();
            if (randomProbability <= cumulativeProbability) {
                return interval;
            }
        }

        return timeIntervals.get(timeIntervals.size() - 1);
    }

    private boolean hasExceededLikeDayLimit(User user) {
        if (user.getLikeDayLimit() != null) {
            LocalDateTime likeDayLimit = user.getLikeDayLimit();
            return likeDayLimit.isAfter(LocalDateTime.now());
        }
        return false;
    }

    private boolean hasExceededUserLikeLimit(User user, LocalDateTime date) {
        var after = LocalDateTime.now().plusHours(24);
        if (!user.isPremium()
                && likeRecordRepository.countAllByUserIdAndLikedAtIsAfter(user.getId(), date) >= USER_LIKE_LIMIT) {
            user.setLikeDayLimit(after);
            return true;
        }
        user.setLikeDayLimit(null);
        return false;
    }

    private static class TimeInterval {
        private final Duration minDuration;
        private final Duration maxDuration;
        private final int probability;

        public TimeInterval(Duration minDuration, Duration maxDuration, int probability) {
            this.minDuration = minDuration;
            this.maxDuration = maxDuration;
            this.probability = probability;
        }

        public Duration getDuration() {
            Random random = new Random();
            long randomDurationSeconds = minDuration.getSeconds() +
                    (long) (random.nextDouble() * (maxDuration.getSeconds() - minDuration.getSeconds()));
            return Duration.ofSeconds(randomDurationSeconds);
        }

        public int getProbability() {
            return probability;
        }
    }

}
