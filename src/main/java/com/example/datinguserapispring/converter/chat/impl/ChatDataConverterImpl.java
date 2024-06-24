package com.example.datinguserapispring.converter.chat.impl;

import com.example.datinguserapispring.converter.chat.ChatDataConverter;
import com.example.datinguserapispring.dto.bot.response.Bot1DTO;
import com.example.datinguserapispring.dto.bot.response.BotProfileSnapshotForAdmin;
import com.example.datinguserapispring.dto.chat.AdminChatDTO;
import com.example.datinguserapispring.dto.chat.MessageContent;
import com.example.datinguserapispring.dto.chat.response.AdminChatResponse;
import com.example.datinguserapispring.dto.chat.response.ProfileSnapshotChatResponse;
import com.example.datinguserapispring.dto.photo.response.PhotoDTO;
import com.example.datinguserapispring.dto.user.UserDTO;
import com.example.datinguserapispring.dto.user.response.ProfileSnapshotSearchResponse;
import com.example.datinguserapispring.mapper.PhotoMapper;
import com.example.datinguserapispring.model.entity.Photo;
import com.example.datinguserapispring.model.entity.bot.Bot2;
import com.example.datinguserapispring.model.entity.dictionary.LookingFor;
import com.example.datinguserapispring.model.entity.user.Address;
import com.example.datinguserapispring.model.entity.user.Block;
import com.example.datinguserapispring.model.entity.user.OnlineStatus;
import com.example.datinguserapispring.model.entity.user.UserLike;
import com.example.datinguserapispring.model.enums.Gender;
import com.example.datinguserapispring.projection.AdminChatProjection;
import com.example.datinguserapispring.repository.BlockRepository;
import com.example.datinguserapispring.repository.LookingForRepository;
import com.example.datinguserapispring.repository.PhotoRepository;
import com.example.datinguserapispring.repository.UserLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatDataConverterImpl implements ChatDataConverter {

    private final PhotoMapper photoMapper;
    private final BlockRepository blockRepository;
    private final PhotoRepository photoRepository;
    private final LookingForRepository lookingForRepository;
    private final UserLikeRepository userLikeRepository;


    public AdminChatResponse createAdminChatResponse(AdminChatProjection chatDTO,
                                                     MessageContent lastMessage,
                                                     UserDTO userDTO,
                                                     BotProfileSnapshotForAdmin botProfileSnapshotForAdmin,
                                                     long unreadMessages) {
        return AdminChatResponse.builder()
                .chatId(chatDTO.getChatId())
                .messageContent(lastMessage)
                .user(userDTO)
                .bot2(botProfileSnapshotForAdmin)
                .unReadMessagesCount(unreadMessages)
                .build();
    }

    public BotProfileSnapshotForAdmin createBot2ProfileSnapShot(AdminChatProjection chatDTO,
                                                                Bot1DTO bot1DTO,
                                                                String adminId,
                                                                List<PhotoDTO> photos,
                                                                boolean isBotBlocked) {
        return BotProfileSnapshotForAdmin.builder()
                .id(chatDTO.getBot2Id())
                .nameBot(chatDTO.getNameBot())
                .adminId(adminId)
                .bot1(bot1DTO)
                .isBotBlocked(isBotBlocked)
                .photoUrls(photos)
                .build();
    }

    public Bot1DTO createBot2DTOFromChatDTO(AdminChatProjection chatDTO) {
        return Bot1DTO.builder()
                .id(chatDTO.getBot1Id())
                .gender(Gender.fromValue(chatDTO.getGender()))
                .race(chatDTO.getRace())
                .minAge(chatDTO.getMinAge())
                .maxAge(chatDTO.getMaxAge())
                .build();
    }

    public List<ProfileSnapshotSearchResponse> createBot2ToProfileSnapshotSearchResponse(String id,
                                                                                         List<Bot2> bot2List,
                                                                                         Address address) {
        return bot2List.stream()
                .map(bot2 -> {
                    Block block = blockRepository.findByBlockingUserIdAndBlockedBotId(id, bot2.getId());
                    boolean isBlocked = block != null && block.isBlocked();
                    List<LookingFor> bot2LookingFor = lookingForRepository.findAllByBot2Id(bot2.getId());
                    UserLike likedYou = userLikeRepository.findUserLikeByBot2IdAndOpponentId(id, bot2.getId());
                    List<String> lookingForList = new ArrayList<>();
                    for (LookingFor lookingFor : bot2LookingFor) {
                        lookingForList.add(lookingFor.getLookingFor());
                    }
                    return ProfileSnapshotSearchResponse.builder()
                            .userId(bot2.getId())
                            .city(address.getCity())
                            .country(address.getCountry())
                            .distance(bot2.getDistance())
                            .isOnline(bot2.getBotOnlineStatus().isOnline())
                            .age(bot2.getAge())
                            .imageLinks(convertToPhotoDTOList(bot2.getId()))
                            .isVerified(true)
                            .isLikedMe(true)
                            .isBlockedMe(false)
                            .isBlocked(isBlocked)
                            .isPremium(true)
                            .lookingForList(lookingForList)
                            .name(bot2.getNameBot())
                            .isLikedYou(likedYou != null && likedYou.isBotLiked())
                            .build();
                })
                .toList();
    }

    public List<PhotoDTO> convertToPhotoDTOList(String bot2Id) {
        List<Photo> photoByBot2Id = photoRepository.findPhotoByBot2Id(bot2Id);
        return photoByBot2Id.stream()
                .map(photoMapper::mapToDTO)
                .toList();
    }

    public List<PhotoDTO> convertToPhotoDTOUserList(List<Photo> photos) {
        return photos.stream()
                .map(photoMapper::mapToDTO)
                .toList();
    }

    public UserDTO createUserDTOFromChatDTO(AdminChatDTO chatDTO,
                                            Address address,
                                            OnlineStatus onlineStatus) {
        return UserDTO.builder()
                .userId(chatDTO.getUserId())
                .gender(chatDTO.getGender())
                .isPremium(chatDTO.isPremium())
                .isBlackList(chatDTO.isBlackList())
                .city(address.getCity())
                .country(address.getCountry())
                .isOnline(onlineStatus.getUser().getOnlineStatus().isOnline())
                .images(convertToPhotoDTOUserList(chatDTO.getChat().getChatMembers().get(0).getUser().getPhotos()))
                .name(chatDTO.getUserName())
                .build();
    }

    public UserDTO createUserDTOFromChatDTO(AdminChatProjection chatDTO,
                                            List<PhotoDTO> photos,
                                            Address address,
                                            OnlineStatus onlineStatus) {
        return UserDTO.builder()
                .userId(chatDTO.getUserId())
                .gender(Gender.fromValue(chatDTO.getGender()))
                .isPremium(chatDTO.getIsPremium())
                .isBlackList(chatDTO.getIsBlackList())
                .city(address.getCity())
                .country(address.getCountry())
                .isOnline(onlineStatus.isOnline())
                .images(photos)
                .name(chatDTO.getName())
                .build();
    }

    public ProfileSnapshotChatResponse createSnapshotChatResponse(Address userAddress,
                                                                  MessageContent lastMessage,
                                                                  Bot2 bot2,
                                                                  boolean isBlocked,
                                                                  List<PhotoDTO> photoDTOList,
                                                                  List<String> lookingFor,
                                                                  boolean isLikedYou,
                                                                  String chatId,
                                                                  boolean isOnline,
                                                                  boolean isMatch, boolean isUserLikedBot) {
        return ProfileSnapshotChatResponse.builder()
                .chatId(chatId)
                .userId(bot2.getId())
                .city(userAddress.getCity())
                .country(userAddress.getCountry())
                .distance(bot2.getDistance())
                .isOnline(isOnline)
                .age(bot2.getAge())
                .imageLinks(photoDTOList)
                .isVerified(true)
                .isLikedMe(isLikedYou)
                .isPremium(true)
                .isBlockedMe(false)
                .isBlocked(isBlocked)
                .isActive(true)
                .isMatch(isMatch)
                .lookingForList(lookingFor)
                .messageContent(lastMessage)
                .name(bot2.getNameBot())
                .isLikedYou(isUserLikedBot)
                .build();
    }
}
