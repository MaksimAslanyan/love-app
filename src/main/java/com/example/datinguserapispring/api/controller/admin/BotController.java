package com.example.datinguserapispring.api.controller.admin;


import com.example.datinguserapispring.constants.UserType;
import com.example.datinguserapispring.dto.bot.request.*;
import com.example.datinguserapispring.dto.user.response.UserInfoResponse;
import com.example.datinguserapispring.dto.bot.response.*;
import com.example.datinguserapispring.dto.photo.response.DeletePhotoResponse;
import com.example.datinguserapispring.dto.photo.response.UploadPhotoResponse;
import com.example.datinguserapispring.exception.Error;
import com.example.datinguserapispring.exception.UserNotFoundException;
import com.example.datinguserapispring.model.entity.Admin;
import com.example.datinguserapispring.model.entity.bot.Bot1;
import com.example.datinguserapispring.repository.AdminRepository;
import com.example.datinguserapispring.security.AppUserDetails;
import com.example.datinguserapispring.security.SecurityContextService;
import com.example.datinguserapispring.service.bot.BotService;
import com.example.datinguserapispring.service.photo.PhotoService;
import com.example.datinguserapispring.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class BotController {

    private final BotService botService;
    private final UserService userService;
    private final PhotoService photoService;
    private final AdminRepository adminRepository;
    private final SecurityContextService securityContextService;


    @GetMapping("/bots/phase1")
    public List<Bot1DTO> listBot1(Pageable pageable) {
        return botService.listBot1(pageable);
    }

    @GetMapping("/{id}/user-info")
    public UserInfoResponse getUserInfo(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @PatchMapping(value = "/{id}/bot/phase2", produces = "application/json")
    public EditBot2Response edit(@PathVariable String id,
                                 @RequestBody EditBot2Request request) {
        return botService.editBot2(id, request);
    }

    @PatchMapping(value = "/bot/phase2", produces = "application/json")
    public AddBot1ToAdminResponse add(@RequestBody AddBot1ToAdminRequest addBot1ToAdminRequest){
        return botService.addBot1ToAdmin(addBot1ToAdminRequest);
    }

    @GetMapping("/{id}/bot/phase2/details")
    public BotProfileSnapshotForAdmin getBotById(@PathVariable String id) {
        return botService.getBotPhase2ById(id);
    }

    @PostMapping("/bots/phase2")
    public List<BotProfileSnapshotForAdmin> getBots2byBot1(@RequestBody BotSearchCriteriaRequest dto,
                                                           Pageable pageable) {
        return botService.getBotsPhase2ByBot1Id(dto, pageable);
    }

    @GetMapping("/bots/phase2")
    public List<BotProfileSnapshotForAdmin> listBot2(Pageable pageable) {
        return botService.listBot2(pageable);
    }

    @PreAuthorize("hasAuthority('OWNER') OR hasAuthority('ADMIN') OR hasAuthority('MODERATOR')")
    @PostMapping("/bot/new/generate")
    public CreateBot1Response createBot1(@RequestBody CreateBot1Request request) {
        return botService.createBot1(request);
    }

    @DeleteMapping(value = "/{id}/bot/phase2", produces = "application/json")
    public DeleteBotResponse deleteBot2(@PathVariable String id) {
        return botService.deleteBot2(id);
    }

    @PreAuthorize("hasAuthority('OWNER') OR hasAuthority('ADMIN') OR hasAuthority('MODERATOR')")
    @PostMapping("/bot2/generate")
    public CreateBot2Response createBot2(@RequestBody CreateBot2Request request) {
        AppUserDetails userDetails = securityContextService.getUserDetails();
        Admin byLogin = adminRepository.findByLogin(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException(Error.USER_NOT_FOUND));
        return botService.createBot2(request, byLogin);
    }

    @PostMapping(value = "/bot/{id}/photo", produces = "application/json")
    public UploadPhotoResponse uploadBotPhoto(
            @PathVariable String id,
            @RequestParam byte photoIndex,
            @RequestParam("photo") MultipartFile photo) {
        return photoService.uploadBotPhoto(id, UserType.BOT, photo, photoIndex);
    }

    @DeleteMapping("/bot/{botId}/photo/{photoId}")
    public DeletePhotoResponse deleteBotPhoto(
            @PathVariable String botId,
            @PathVariable String photoId) {
        return photoService.deleteBot2Photo(photoId, botId);
    }
}
