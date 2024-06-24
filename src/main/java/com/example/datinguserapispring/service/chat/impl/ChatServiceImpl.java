package com.example.datinguserapispring.service.chat.impl;

import com.example.datinguserapispring.converter.chat.ChatDataConverter;
import com.example.datinguserapispring.dto.UserAndBotIds;
import com.example.datinguserapispring.dto.UserAndBotPhoto;
import com.example.datinguserapispring.dto.bot.response.Bot1DTO;
import com.example.datinguserapispring.dto.bot.response.BotProfileSnapshotForAdmin;
import com.example.datinguserapispring.dto.chat.AdminChatDTO;
import com.example.datinguserapispring.dto.chat.AdminSortedChat;
import com.example.datinguserapispring.dto.chat.ChatCategory;
import com.example.datinguserapispring.dto.chat.MessageContent;
import com.example.datinguserapispring.dto.chat.MessageDTO;
import com.example.datinguserapispring.dto.chat.UnreadMessagesCountDTO;
import com.example.datinguserapispring.dto.chat.request.ChatRequest;
import com.example.datinguserapispring.dto.chat.response.AdminChatResponse;
import com.example.datinguserapispring.dto.chat.response.ChatResponse;
import com.example.datinguserapispring.dto.chat.response.ProfileSnapshotChatResponse;
import com.example.datinguserapispring.dto.chat.response.UnreadMessagesCountsResponse;
import com.example.datinguserapispring.dto.photo.response.PhotoDTO;
import com.example.datinguserapispring.dto.user.UserDTO;
import com.example.datinguserapispring.dto.user.response.ProfileSnapshotSearchResponse;
import com.example.datinguserapispring.exception.AuthenticationException;
import com.example.datinguserapispring.exception.Error;
import com.example.datinguserapispring.model.entity.Admin;
import com.example.datinguserapispring.model.entity.Location;
import com.example.datinguserapispring.model.entity.bot.Bot2;
import com.example.datinguserapispring.model.entity.chat.Chat;
import com.example.datinguserapispring.model.entity.chat.ChatMember;
import com.example.datinguserapispring.model.entity.chat.ChatMemberAdmin;
import com.example.datinguserapispring.model.entity.chat.ChatMessage;
import com.example.datinguserapispring.model.entity.user.Address;
import com.example.datinguserapispring.model.entity.user.Block;
import com.example.datinguserapispring.model.entity.user.OnlineStatus;
import com.example.datinguserapispring.model.entity.user.UserLike;
import com.example.datinguserapispring.model.enums.ChatType;
import com.example.datinguserapispring.projection.AdminChatProjection;
import com.example.datinguserapispring.projection.ChatProjection;
import com.example.datinguserapispring.projection.LookingForProjection;
import com.example.datinguserapispring.repository.AddressRepository;
import com.example.datinguserapispring.repository.AdminRepository;
import com.example.datinguserapispring.repository.BlockRepository;
import com.example.datinguserapispring.repository.ChatMemberAdminRepository;
import com.example.datinguserapispring.repository.ChatMemberRepository;
import com.example.datinguserapispring.repository.ChatMessageRepository;
import com.example.datinguserapispring.repository.ChatRepository;
import com.example.datinguserapispring.repository.LocationRepository;
import com.example.datinguserapispring.repository.LookingForRepository;
import com.example.datinguserapispring.repository.PhotoRepository;
import com.example.datinguserapispring.repository.SearchChatRepository;
import com.example.datinguserapispring.repository.UserLikeRepository;
import com.example.datinguserapispring.service.chat.ChatService;
import com.example.datinguserapispring.util.ZoneDateIdConversionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final PhotoRepository photoRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final AddressRepository addressRepository;
    private final ChatMemberAdminRepository chatMemberAdminRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final AdminRepository adminRepository;
    private final BlockRepository blockRepository;
    private final SearchChatRepository searchChatRepository;
    private final LookingForRepository lookingForRepository;
    private final LocationRepository locationRepository;
    private final UserLikeRepository userLikeRepository;
    private final ChatDataConverter converter;

    @Override
    public AdminSortedChat findAllAdminChats(ChatCategory chatCategory, String login, Pageable pageable) {
        Admin admin = adminRepository.findByLogin(login)
                .orElseThrow(() -> new AuthenticationException(Error.USER_NOT_FOUND));
        String adminId = admin.getId();
        String chatTypeValue = chatCategory.getChatType() != null ? chatCategory.getChatType().getValue() : "";
        List<AdminChatProjection> chatsByAdminIdAndChatType = checkChatType(chatCategory, pageable, adminId, chatTypeValue);

        UserAndBotIds userAndBotIds = extractIdsFromAdminProjection(chatsByAdminIdAndChatType);

        UserAndBotPhoto userAndBotPhoto = userAndBotPhotos(userAndBotIds);

        List<AdminChatResponse> chats = new ArrayList<>();
        long globalUnreadMessages = 0;
        for (AdminChatProjection chatDTO : chatsByAdminIdAndChatType) {

            long totalUnreadMessages =
                    getTotalUnreadMessagesForChatAndBot2(chatDTO.getChatId(), chatDTO.getBot2Id());
            globalUnreadMessages += totalUnreadMessages;

            AdminChatResponse adminChatContent =
                    createAdminChatContent(chatDTO, userAndBotPhoto.userPhotos(), userAndBotPhoto.bot2Photos(), adminId, totalUnreadMessages);

            chats.add(adminChatContent);
        }

        AdminSortedChat sortedChat = new AdminSortedChat();
        sortedChat.setGlobalUnreadChatMessages(globalUnreadMessages);
        sortedChat.setSortedList(chats);

        return sortedChat;
    }

    @Override
    public AdminSortedChat findNameFromChats(String login, Pageable pageable, String userName) {
        Admin admin = adminRepository.findByLogin(login)
                .orElseThrow(() -> new AuthenticationException(Error.USER_NOT_FOUND));
        String adminId = admin.getId();

        List<AdminChatProjection> chatsByAdminIdAndChatType =
                chatRepository.findChatsByAdminIdAndUserName(adminId, userName, pageable);
        UserAndBotIds userAndBotIds = extractIdsFromAdminProjection(chatsByAdminIdAndChatType);

        UserAndBotPhoto userAndBotPhoto = userAndBotPhotos(userAndBotIds);

        List<AdminChatResponse> chats = new ArrayList<>();
        long globalUnreadMessages = 0;
        for (AdminChatProjection chatDTO : chatsByAdminIdAndChatType) {
            long totalUnreadMessages =
                    getTotalUnreadMessagesForChatAndBot2(chatDTO.getChatId(), chatDTO.getBot2Id());
            globalUnreadMessages += totalUnreadMessages;

            AdminChatResponse adminChatContent =
                    createAdminChatContent(chatDTO, userAndBotPhoto.userPhotos(), userAndBotPhoto.bot2Photos(), adminId, totalUnreadMessages);

            chats.add(adminChatContent);
        }
        AdminSortedChat sortedChat = new AdminSortedChat();
        sortedChat.setGlobalUnreadChatMessages(globalUnreadMessages);
        sortedChat.setSortedList(chats);

        return sortedChat;
    }

    @Override
    public UnreadMessagesCountsResponse findAllUnreadMessages(String login, Pageable pageable) {
        Admin admin = adminRepository.findByLogin(login)
                .orElseThrow(() -> new AuthenticationException(Error.USER_NOT_FOUND));

        List<AdminChatDTO> allChatsForAdmin = searchChatRepository.findUnreadMessages(admin.getId(), pageable);

        Map<String, BigInteger> chatTypeCountsMap = calculateUnreadMessageCountsByChatType(allChatsForAdmin);

        return createUnreadMessagesCountsResponse(chatTypeCountsMap);
    }

    @Override
    public List<MessageDTO> findChatMessagesUser(ChatRequest chat, Pageable pageable, String id) {
        Page<ChatMessage> chatMessages = chatMessageRepository
                .findByChatIdOrderByCreatedDateDesc(chat.getId(), pageable);

        Location firstByUserIdOrderByCreatedAtDesc = locationRepository
                .findFirstByUserIdOrderByCreatedAtDesc(id).orElse(null);

        String timeZone = null;
        if (firstByUserIdOrderByCreatedAtDesc != null) {
            timeZone = firstByUserIdOrderByCreatedAtDesc.getTimeZone();
        }

        String finalTimeZone = timeZone;
        return chatMessages.stream()
                .map(chatMessage -> mapToDTO(chatMessage, finalTimeZone))
                .toList();
    }

    @Override
    public UnreadMessagesCountDTO convertToUnreadMessagesCountDTOList(List<Object[]> resultList) {
        UnreadMessagesCountDTO unreadMessagesCountDTO = new UnreadMessagesCountDTO();
        for (Object[] result : resultList) {
            String chatType = (String) result[0];
            BigInteger count = (BigInteger) result[1];
            unreadMessagesCountDTO.setCounts(count);
            unreadMessagesCountDTO.setChatType(chatType);
        }
        return unreadMessagesCountDTO;
    }

    @Override
    public List<ProfileSnapshotSearchResponse> findMostFrequentChats(String id) {
        Address userAddress = addressRepository.getFirstByUserId(id);
        List<ChatMember> chatMembers = chatMemberRepository.findChatMemberByUserId(id);

        // Create a map to store chat IDs and their message counts
        Map<String, Long> chatMessageCounts = new HashMap<>();

        for (ChatMember chatMember : chatMembers) {
            Chat chat = chatMember.getChat();
            if (chat.getCreatedDate().isBefore(LocalDateTime.now())) {
                long chatMessages = chatMessageRepository.countByChatId(chat.getId());
                if (chatMessages > 0 || chat.isMatch()) {
                    chatMessageCounts.put(chat.getId(), chatMessages);
                }
            }
        }

        // Sort the chats by message count in descending order
        List<String> sortedChatIds = chatMessageCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(3) // Limit to the top 4 chats with the highest message count
                .map(Map.Entry::getKey)
                .toList();

        List<Bot2> chatMemberAdminsByChatId = new ArrayList<>();

        // Fetch the chat members for the sorted chat IDs
        for (String chatId : sortedChatIds) {
            List<ChatMemberAdmin> chatMemberAdminsByChatId1 =
                    chatMemberAdminRepository.findChatMemberAdminsByChatId(chatId);
            for (ChatMemberAdmin chatMemberAdmin : chatMemberAdminsByChatId1) {
                chatMemberAdminsByChatId.add(chatMemberAdmin.getBot2());
            }
        }

        return converter.createBot2ToProfileSnapshotSearchResponse(
                id,
                chatMemberAdminsByChatId,
                userAddress);
    }

    @Override
    public List<ProfileSnapshotChatResponse> findAllChats(String id, Pageable pageable) {
        long startTime = System.currentTimeMillis();
        log.info("Fetching user chats Mobile for id: {}", id);
        List<ChatProjection> chats = chatRepository.findChatsByUserId(LocalDateTime.now(), id, pageable);

        Set<String> bot2Ids = chats.stream()
                .map(ChatProjection::getBot2Id)
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
        Address address = addressRepository.findTop1ByUserId(id);
        Location location = locationRepository.findFirstByUserIdOrderByCreatedAtDesc(id).orElse(null);
        List<ProfileSnapshotChatResponse> chatResponses = new ArrayList<>();

        for (ChatProjection chatMetaData : chats) {
            String chatId = chatMetaData.getChatId();
            boolean isMatch = chatMetaData.getIsMatch();

            ChatMessage lastMessage = new ChatMessage(chatMetaData.getChatMessageId(), chatMetaData.getLastMessage(), chatMetaData.getLastMessageDate(), chatMetaData.getPhotoId(), chatMetaData.getIsRead() != null ? chatMetaData.getIsRead() : false, chatMetaData.getChatMember(), chatMetaData.getChatMemberAdmin());

            MessageContent messageContent = (id != null)
                    ? mapToMessageContentForUser(lastMessage, location.getTimeZone(), id)
                    : null;

            Bot2 bot2 = new Bot2(chatMetaData.getBot2Id(), chatMetaData.getName(), chatMetaData.getAge(), chatMetaData.getDistance());
            Block block = blockRepository.findByBlockingUserIdAndBlockedBotId(id, bot2.getId());
            boolean isOnline = chatMetaData.getIsOnline();
            UserLike likedYou = userLikeRepository.findUserLikeByBot2IdAndOpponentId(id, bot2.getId());
            boolean isLikedYou = likedYou != null;

            boolean isBlocked = block != null && block.isBlocked();

            ProfileSnapshotChatResponse chatResponse = converter.createSnapshotChatResponse(
                    address, messageContent,
                    bot2, isBlocked, photoMap.get(bot2.getId()),
                    lookingForsMap.get(bot2.getId()), isLikedYou, chatId,
                    isOnline, isMatch, likedYou != null && likedYou.isBotLiked());

            chatResponses.add(chatResponse);

        }
        long endTime = System.currentTimeMillis();
        log.info("Execution time of got chats Mobile: {} seconds", (endTime - startTime) / 1000.0);
        return chatResponses;
    }

    @Override
    public List<MessageDTO> findChatMessagesAdmin(ChatRequest chat, Pageable pageable, String userId) {
        Page<ChatMessage> chatMessages = chatMessageRepository.findByChatIdOrderByCreatedDateDesc(chat.getId(), pageable);
        return chatMessages.stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    @Transactional
    public ChatResponse deleteChat(String chatId) {
        chatRepository.deleteById(chatId);
        return new ChatResponse(chatId);
    }

    @Override
    public Chat save(Chat chat) {
        return chatRepository.save(chat);
    }

    private UserAndBotPhoto userAndBotPhotos(UserAndBotIds userAndBotIds) {
        Map<String, List<PhotoDTO>> userPhotos =
                extractPhotosByUserIds(userAndBotIds.userIds());
        Map<String, List<PhotoDTO>> bot2Photos =
                extractPhotosByBot2Ids(userAndBotIds.bot2Ids());

        return new UserAndBotPhoto(userPhotos, bot2Photos);
    }

    private List<AdminChatProjection> checkChatType(ChatCategory chatCategory, Pageable pageable, String adminId, String chatTypeValue) {
        List<AdminChatProjection> chatsByAdminIdAndChatType;
        if (ChatType.NULL == chatCategory.getChatType()) {
            chatsByAdminIdAndChatType = chatRepository.findChatsByAdminIdAndChatTypeIsNull(adminId, chatTypeValue, pageable);
        } else {
            chatsByAdminIdAndChatType = chatRepository.findChatsByAdminIdAndChatType(adminId, chatTypeValue, pageable);
        }
        return chatsByAdminIdAndChatType;
    }

    private Map<String, BigInteger> calculateUnreadMessageCountsByChatType(List<AdminChatDTO> chatDTOList) {
        Map<String, BigInteger> chatTypeCountsMap = new HashMap<>();

        for (AdminChatDTO chatDTO : chatDTOList) {
            UnreadMessagesCountDTO unreadMessagesCountDTO = convertToUnreadMessagesCountDTO(chatDTO);

            String chatType = unreadMessagesCountDTO.getChatType();
            BigInteger counts = unreadMessagesCountDTO.getCounts();

            if (chatType != null && counts != null) {
                chatTypeCountsMap.putIfAbsent(chatType, BigInteger.ZERO);
                chatTypeCountsMap.put(chatType, chatTypeCountsMap.get(chatType).add(counts));
            }
        }

        return chatTypeCountsMap;
    }

    private UnreadMessagesCountDTO convertToUnreadMessagesCountDTO(AdminChatDTO chatDTO) {
        return convertToUnreadMessagesCountDTOList(
                chatMessageRepository.countUnreadMessagesByChatTypeAndChatId(
                        chatDTO.getChat().getId(),
                        chatDTO.getBot2Id()
                )
        );
    }

    private UnreadMessagesCountsResponse createUnreadMessagesCountsResponse(Map<String, BigInteger> chatTypeCountsMap) {
        return UnreadMessagesCountsResponse.builder()
                .premium(chatTypeCountsMap.getOrDefault(ChatType.IS_PREMIUM.getValue(), BigInteger.ZERO).longValue())
                .lesbian(chatTypeCountsMap.getOrDefault(ChatType.LESBIAN.getValue(), BigInteger.ZERO).longValue())
                .gay(chatTypeCountsMap.getOrDefault(ChatType.GAY.getValue(), BigInteger.ZERO).longValue())
                .inactive(chatTypeCountsMap.getOrDefault(ChatType.INACTIVE.getValue(), BigInteger.ZERO).longValue())
                .maleFemale(chatTypeCountsMap.getOrDefault(ChatType.MALE_FEMALE.getValue(), BigInteger.ZERO).longValue())
                .femaleMale(chatTypeCountsMap.getOrDefault(ChatType.FEMALE_MALE.getValue(), BigInteger.ZERO).longValue())
                .build();
    }

    private long getTotalUnreadMessagesForChatAndBot2(String chatId, String bot2Id) {
        return chatMessageRepository.countUnreadMessagesForChatAndBot2(chatId, bot2Id);
    }

    private MessageDTO mapToDTO(ChatMessage chatMessage) {
        String userId = chatMessage.getChatMemberAdmin() != null
                && chatMessage.getChatMemberAdmin().getBot2() != null
                ? chatMessage.getChatMemberAdmin().getBot2().getId()
                : null;

        return MessageDTO.builder()
                .id(chatMessage.getId())
                .userId(userId)
                .createdDate(chatMessage.getCreatedDate())
                .updatedDate(chatMessage.getUpdatedDate())
                .message(chatMessage.getMessage())
                .photoId(chatMessage.getPhotoId())
                .isRead(chatMessage.isRead())
                .canSee(chatMessage.isCanSee())
                .build();
    }

    private MessageDTO mapToDTO(ChatMessage chatMessage, String timeZone) {
        String userId = chatMessage.getChatMember() != null
                && chatMessage.getChatMember().getUser() != null
                ? chatMessage.getChatMember().getUser().getId()
                : null;

        LocalDateTime zoneCreatedDate;
        if (timeZone != null) {
            zoneCreatedDate =
                    ZoneDateIdConversionUtil.performTimeConversion(chatMessage.getCreatedDate(), timeZone);
        } else {
            zoneCreatedDate = chatMessage.getCreatedDate();
        }

        return MessageDTO.builder()
                .id(chatMessage.getId())
                .userId(userId)
                .createdDate(zoneCreatedDate)
                .updatedDate(chatMessage.getUpdatedDate())
                .message(chatMessage.getMessage())
                .photoId(chatMessage.getPhotoId())
                .isRead(chatMessage.isRead())
                .canSee(chatMessage.isCanSee())
                .build();
    }

    private MessageContent mapToMessageContent(ChatMessage chatMessage, String timeZone, String bot2Id) {
        LocalDateTime zoneCreatedDate;
        if (timeZone != null) {
            zoneCreatedDate = ZoneDateIdConversionUtil.performTimeConversion(chatMessage.getCreatedDate(), timeZone);
        } else {
            zoneCreatedDate = chatMessage.getCreatedDate();
        }

        if (chatMessage.getChatMemberAdmin().getId() == null) {
            bot2Id = null;
        }

        Boolean isRead = (chatMessage.getMessage() != null || chatMessage.getPhotoId() != null) ? true : null;
        if (chatMessage.getMessage() == null && chatMessage.getPhotoId() == null) {
            isRead = null;
        }

        return MessageContent.builder()
                .userId(bot2Id)
                .message(chatMessage.getMessage())
                .createdDate(zoneCreatedDate)
                .photoId(chatMessage.getPhotoId())
                .read(isRead)
                .build();
    }

    private MessageContent mapToMessageContentForUser(ChatMessage chatMessage, String timeZone, String userId) {
        LocalDateTime zoneCreatedDate;

        if (timeZone != null && chatMessage.getCreatedDate() != null) {
            ZoneId zoneId = ZoneId.of(timeZone);
            zoneCreatedDate = chatMessage.getCreatedDate().atZone(ZoneId.systemDefault()).withZoneSameInstant(zoneId).toLocalDateTime();
        } else {
            zoneCreatedDate = chatMessage.getCreatedDate();
        }


        if (chatMessage.getChatMember().getId() == null) {
            userId = null;
        }

        return MessageContent.builder()
                .userId(userId)
                .message(chatMessage.getMessage())
                .createdDate(zoneCreatedDate)
                .photoId(chatMessage.getPhotoId())
                .read(chatMessage.isRead())
                .build();
    }

    private Map<String, List<PhotoDTO>> extractPhotosByBot2Ids(Set<String> bot2Ids) {
        return photoRepository.mapToPhotoDTO(photoRepository.findPhotoByBot2IdIn(bot2Ids));
    }

    private Map<String, List<PhotoDTO>> extractPhotosByUserIds(Set<String> userIds) {
        return photoRepository.mapToUserPhotoDTO(photoRepository.findPhotoByUserIdIn(userIds));
    }

    private UserAndBotIds extractIdsFromAdminProjection(List<AdminChatProjection> chatsByAdminIdAndChatType) {

        Set<String> bot2Ids = chatsByAdminIdAndChatType.stream()
                .map(AdminChatProjection::getBot2Id)
                .collect(Collectors.toSet());

        Set<String> userIds = chatsByAdminIdAndChatType.stream()
                .map(AdminChatProjection::getUserId)
                .collect(Collectors.toSet());
        return new UserAndBotIds(bot2Ids, userIds);
    }

    private AdminChatResponse createAdminChatContent(AdminChatProjection chatDTO,
                                                     Map<String, List<PhotoDTO>> userPhotos,
                                                     Map<String, List<PhotoDTO>> bot2Photos,
                                                     String adminId, long totalUnreadMessages) {
        String timeZone = null;
        ChatMessage lastMessage = new ChatMessage(chatDTO.getChatMessageId(), chatDTO.getLastMessage(), chatDTO.getLastMessageDate(), chatDTO.getPhotoId(), chatDTO.getIsRead() != null ? chatDTO.getIsRead() : false, chatDTO.getChatMemberId(), chatDTO.getChatMemberId());
        MessageContent messageContent = (chatDTO.getBot2Id() != null)
                ? mapToMessageContent(lastMessage, timeZone, chatDTO.getBot2Id())
                : null;

        boolean isBotBlocked = blockRepository.existsByBlockingUserIdAndBlockedBotId(chatDTO.getUserId(), chatDTO.getBot2Id());
        Address address = new Address(chatDTO.getCity(), chatDTO.getCountry());
        OnlineStatus online = OnlineStatus.builder().isOnline(chatDTO.getIsOnline()).build();
        UserDTO userDTO = converter.createUserDTOFromChatDTO(chatDTO, userPhotos.get(chatDTO.getUserId()), address, online);
        Bot1DTO bot1DTO = converter.createBot2DTOFromChatDTO(chatDTO);
        BotProfileSnapshotForAdmin botProfileSnapshotForAdmin = converter
                .createBot2ProfileSnapShot(chatDTO, bot1DTO, adminId, bot2Photos.get(chatDTO.getBot2Id()), isBotBlocked);

        return converter.createAdminChatResponse(
                chatDTO,
                messageContent,
                userDTO,
                botProfileSnapshotForAdmin,
                totalUnreadMessages);
    }

}
