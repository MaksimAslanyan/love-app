package com.example.datinguserapispring.converter.chat;

import com.example.datinguserapispring.dto.bot.response.Bot1DTO;
import com.example.datinguserapispring.dto.bot.response.BotProfileSnapshotForAdmin;
import com.example.datinguserapispring.dto.chat.MessageContent;
import com.example.datinguserapispring.dto.chat.response.AdminChatResponse;
import com.example.datinguserapispring.dto.chat.response.ProfileSnapshotChatResponse;
import com.example.datinguserapispring.dto.photo.response.PhotoDTO;
import com.example.datinguserapispring.dto.user.UserDTO;
import com.example.datinguserapispring.dto.user.response.ProfileSnapshotSearchResponse;
import com.example.datinguserapispring.model.entity.bot.Bot2;
import com.example.datinguserapispring.model.entity.user.Address;
import com.example.datinguserapispring.model.entity.user.OnlineStatus;
import com.example.datinguserapispring.projection.AdminChatProjection;

import java.util.List;

public interface ChatDataConverter {

    AdminChatResponse createAdminChatResponse(AdminChatProjection chatDTO,
                                              MessageContent lastMessage,
                                              UserDTO userDTO,
                                              BotProfileSnapshotForAdmin botProfileSnapshotForAdmin,
                                              long unreadMessages);

    BotProfileSnapshotForAdmin createBot2ProfileSnapShot(AdminChatProjection chatDTO, Bot1DTO bot1DTO, String adminId, List<PhotoDTO> photos, boolean isBotBlocked);

    Bot1DTO createBot2DTOFromChatDTO(AdminChatProjection chatDTO);

    //    UserDTO createUserDTOFromChatDTO(AdminChatDTO chatDTO, Address address, OnlineStatus onlineStatus);
    UserDTO createUserDTOFromChatDTO(AdminChatProjection chatDTO, List<PhotoDTO> photos, Address address, OnlineStatus onlineStatus);

    List<ProfileSnapshotSearchResponse> createBot2ToProfileSnapshotSearchResponse(String id,
                                                                                  List<Bot2> bot2List,
                                                                                  Address address);

    ProfileSnapshotChatResponse createSnapshotChatResponse(Address userAddress,
                                                           MessageContent lastMessage,
                                                           Bot2 bot2,
                                                           boolean isBlocked,
                                                           List<PhotoDTO> photoDTOList,
                                                           List<String> lookingFor,
                                                           boolean isLikedYou,
                                                           String chatId,
                                                           boolean isOnline,
                                                           boolean isMatch,
                                                           boolean isUserLikedBot);
}
