package com.example.datinguserapispring.service.bot.impl;


import com.example.datinguserapispring.dto.bot.request.*;
import com.example.datinguserapispring.dto.bot.response.*;
import com.example.datinguserapispring.dto.photo.response.PhotoDTO;
import com.example.datinguserapispring.exception.Bot2NotFoundException;
import com.example.datinguserapispring.exception.EntityNotFoundException;
import com.example.datinguserapispring.exception.Error;
import com.example.datinguserapispring.exception.UserNotFoundException;
import com.example.datinguserapispring.filter.specification.Bot2Specification;
import com.example.datinguserapispring.mapper.BotMapper;
import com.example.datinguserapispring.mapper.PhotoMapper;
import com.example.datinguserapispring.model.entity.Admin;
import com.example.datinguserapispring.model.entity.Photo;
import com.example.datinguserapispring.model.entity.bot.Bot1;
import com.example.datinguserapispring.model.entity.bot.Bot2;
import com.example.datinguserapispring.model.enums.ParentType;
import com.example.datinguserapispring.repository.AdminRepository;
import com.example.datinguserapispring.repository.Bot1Repository;
import com.example.datinguserapispring.repository.Bot2Repository;
import com.example.datinguserapispring.repository.PhotoRepository;
import com.example.datinguserapispring.service.bot.BotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class BotServiceImpl implements BotService {

    private final Bot1Repository bot1Repository;
    private final BotMapper botMapper;
    private final AdminRepository adminRepository;
    private final PhotoRepository photoRepository;
    private final PhotoMapper photoMapper;
    private final Bot2Repository bot2Repository;

    @Override
    public CreateBot1Response createBot1(CreateBot1Request request) {
        log.debug("Creating Bot1...");
        Bot1 bot1 = new Bot1();
        createBot1(request, bot1);
        Bot1 savedBot1 = bot1Repository.save(bot1);
        log.debug("Bot1 created with ID: {}", savedBot1.getId());

        return new CreateBot1Response(savedBot1.getId());

    }

    @Override
    public List<BotProfileSnapshotForAdmin> getBotsPhase2ByBot1Id(BotSearchCriteriaRequest dto, Pageable pageable) {
        String bot1Id = dto.getBot1Id();
        String bot2Id = dto.getBot2Id();
        log.debug("Fetching Bot2 by Bot1 ID: {}", bot1Id);

        List<BotProfileSnapshotForAdmin> botProfileSnapshots = null;

        if (!bot1Id.isEmpty()) {
            Page<Bot2> bot2s = bot2Repository
                    .findAllByBot1IdAndParentType(bot1Id, ParentType.OWNER, pageable);

            botProfileSnapshots = bot2s.getContent().stream()
                    .flatMap(bot2 -> getProfileSnapshotForAdmins(bot2).stream())
                    .toList();
        }
        if (!(bot1Id.isEmpty() && bot2Id.isEmpty())) {
            Specification<Bot2> spec = Bot2Specification
                    .findByBot1IdBot2IdAndParentType(bot1Id, bot2Id, ParentType.OWNER);

            Page<Bot2> page = bot2Repository.findAll(spec, pageable);
            botProfileSnapshots = page.getContent().stream()
                    .flatMap(bot2 -> getProfileSnapshotForAdmins(bot2).stream())
                    .toList();
        }

        log.debug("BotsPhase2 fetched for Bot1 ID: {}", bot1Id);
        return botProfileSnapshots;
    }

    @Override
    public EditBot2Response editBot2(String id, EditBot2Request request) {
        log.debug("Editing Bot2 with ID: {}", id);
        Bot2 bot2 = bot2Repository.findById(id)
                .orElseThrow(() -> new Bot2NotFoundException(Error.BOT2_NOT_FOUND));

        if (request.getName() != null) {
            bot2.setNameBot(request.getName());
        }
        bot2Repository.save(bot2);

        log.debug("Bot2 edited successfully with ID: {}", id);
        return new EditBot2Response(id);
    }

    @Override
    public BotProfileSnapshotForAdmin getBotPhase2ById(String id) {
        log.debug("Fetching Bot2 by ID: {}", id);
        Bot2 bot2 = bot2Repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(Error.USER_NOT_FOUND));
        Bot1 bot1 = bot1Repository.findById(bot2.getBot1().getId())
                .orElseThrow(() -> new UserNotFoundException(Error.USER_NOT_FOUND));
        Admin admin = adminRepository.findById(bot2.getAdmin().getId())
                .orElseThrow(() -> new UserNotFoundException(Error.USER_NOT_FOUND));

        List<Photo> allByUserId = photoRepository.findPhotoByBot2Id(bot2.getId());
        List<PhotoDTO> photoUrls = allByUserId.stream()
                .map(photoMapper::mapToDTO)
                .toList();

        Bot1DTO bot1DTO = botMapper.mapToBot1DTO(bot1);
        log.debug("BotProfileSnapshotForAdmin created successfully for Bot2 ID: {}", bot2.getId());
        return BotProfileSnapshotForAdmin.builder()
                .id(bot2.getId())
                .nameBot(bot2.getNameBot())
                .adminId(admin.getId())
                .bot1(bot1DTO)
                .photoUrls(photoUrls)
                .build();
    }

    @Override
    public AddBot1ToAdminResponse addBot1ToAdmin(AddBot1ToAdminRequest addBot1ToAdminRequest) {
        String adminId = addBot1ToAdminRequest.getAdminId();
        String bot1Id = addBot1ToAdminRequest.getBot1Id();
        log.debug("Add Bot1 to Admin by ID: {}", adminId);

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException(Error.ENTITY_NOT_FOUND));

        Bot1 existingBot1 = bot1Repository.findById(bot1Id)
                .orElseThrow(() -> new EntityNotFoundException(Error.ENTITY_NOT_FOUND));

        Bot1 newBot1 = createBot1ForAdmin(admin, existingBot1);

        log.debug("Bot1 added successfully for Admin BOT ID: {}", newBot1.getId());
        return new AddBot1ToAdminResponse(adminId);
    }

    @Override
    public CreateBot2Response createBot2(CreateBot2Request request, Admin admin) {
        Bot2 bot2 = Bot2.builder()
                .id(UUID.randomUUID().toString())
                .admin(admin)
                .bot1(bot1Repository.findById(request.getBot1Id())
                        .orElseThrow(() -> new Bot2NotFoundException(Error.BOT2_NOT_FOUND)))
                .nameBot(request.getName())
                .build();

        Bot2 savedBot2 = bot2Repository.save(bot2);

        return new CreateBot2Response(savedBot2.getId());
    }

    @Override
    public List<Bot1DTO> listBot1(Pageable pageable) {
        ParentType parentType = ParentType.valueOf(ParentType.OWNER.getValue());
        Page<Bot1> bot1s = bot1Repository.findAllByParentTypeAndRace(parentType, "White", pageable);

        return bot1s.stream()
                .map(botMapper::mapToBot1DTO)
                .toList();
    }

    @Override
    public List<BotProfileSnapshotForAdmin> listBot2(Pageable pageable) {
        Page<Bot2> bot2s = bot2Repository.findAll(pageable);
        return bot2s.stream()
                .flatMap(botItem -> getProfileSnapshotForAdmins(botItem).stream())
                .toList();
    }

    @Transactional
    @Override
    public DeleteBotResponse deleteBot2(String botId) {
        bot2Repository.deleteById(botId);
        return new DeleteBotResponse(botId);
    }

    private Bot1 createBot1ForAdmin(Admin admin, Bot1 existingBot1) {
        Bot1 newBot1 = Bot1.builder()
                .gender(existingBot1.getGender())
                .race(existingBot1.getRace())
                .minAge(existingBot1.getMinAge())
                .maxAge(existingBot1.getMaxAge())
                .identity(existingBot1.getId())
                .admin(admin)
                .parentType(ParentType.ADMIN)
                .build();
        newBot1 = bot1Repository.save(newBot1);
        return newBot1;
    }

    private void createBot1(CreateBot1Request request, Bot1 bot1) {
        bot1.setGender(request.getGender());
        bot1.setRace(String.valueOf(request.getRace()));
        bot1.setMaxAge(request.getMaxAge());
        bot1.setMinAge(request.getMinAge());
    }

    private Bot1DTO getBot1FromBot2(Bot2 botItem) {
        return Bot1DTO.builder()
                .id(botItem.getBot1().getId())
                .gender(botItem.getBot1().getGender())
                .race(botItem.getBot1().getRace())
                .minAge(botItem.getBot1().getMinAge())
                .maxAge(botItem.getBot1().getMaxAge())
                .build();
    }

    private List<BotProfileSnapshotForAdmin> getProfileSnapshotForAdmins(Bot2 bot2) {
        List<Photo> botPhoto = new ArrayList<>(photoRepository.findPhotoByBot2Id(bot2.getId()));
        List<PhotoDTO> photoUrls = botPhoto.stream()
                .map(photoMapper::mapToDTO)
                .toList();

        Bot1DTO bot1DTO = getBot1FromBot2(bot2);

        return List.of(
                BotProfileSnapshotForAdmin.builder()
                        .id(bot2.getId())
                        .nameBot(bot2.getNameBot())
                        .adminId(bot2.getAdmin().getId())
                        .bot1(bot1DTO)
                        .photoUrls(photoUrls)
                        .build()
        );
    }
}
