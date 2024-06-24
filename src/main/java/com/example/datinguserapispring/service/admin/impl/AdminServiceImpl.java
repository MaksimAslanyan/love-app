package com.example.datinguserapispring.service.admin.impl;

import com.example.datinguserapispring.dto.admin.request.CreateAdminRequest;
import com.example.datinguserapispring.dto.admin.request.EditAdminRequest;
import com.example.datinguserapispring.dto.admin.request.LoginAdminRequest;
import com.example.datinguserapispring.dto.admin.response.*;
import com.example.datinguserapispring.dto.photo.response.DeletePhotoResponse;
import com.example.datinguserapispring.dto.photo.response.PhotoDTO;
import com.example.datinguserapispring.exception.AdminAlreadyExistException;
import com.example.datinguserapispring.exception.EntityNotFoundException;
import com.example.datinguserapispring.exception.Error;
import com.example.datinguserapispring.exception.UserNotFoundException;
import com.example.datinguserapispring.manager.JwtTokenManager;
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
import com.example.datinguserapispring.service.admin.AdminService;
import liquibase.util.MD5Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private static final String DELETED = "-DELETED";

    private final AdminRepository adminRepository;
    private final Bot1Repository bot1Repository;
    private final PhotoRepository photoRepository;
    private final Bot2Repository bot2Repository;
    private final PhotoMapper photoMapper;
    private final JwtTokenManager jwtTokenManager;


    @Override
    public CreateAdminResponse createAdmin(CreateAdminRequest request) {
        Admin admin = new Admin();
        admin.setId(String.valueOf(UUID.randomUUID()));
        admin.setLogin(request.getLogin());
        admin.setPassword(MD5Util.computeMD5(request.getPassword()));
        admin.setNickName(request.getTg());
        admin.setName(request.getName());
        admin.setRole(request.getAuthority());
        admin.setAge(request.getAge());

        boolean exists = adminRepository.existsByLoginOrNickName(admin.getLogin(), admin.getNickName());
        if (exists) {
            throw new AdminAlreadyExistException(Error.ADMIN_ALREADY_EXIST);
        }

        Admin savedAdmin = adminRepository.save(admin);
        if (savedAdmin.getId() == null) {
            throw new RuntimeException("Failed to save admin");
        }

        return new CreateAdminResponse(savedAdmin.getId());
    }

    @Override
    public List<ListAdminItemResponse> adminList() {
        log.info("Fetching Admins");
        List<Admin> activeAdmins = adminRepository.findAllByIsDeletedIsFalse();
        return activeAdmins.stream()
                .map(this::converter)
                .toList();
    }

    @Override
    public EditAdminResponse editAdmin(String adminId, EditAdminRequest request) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new UserNotFoundException(Error.USER_NOT_FOUND));

        if (request.getName() != null) {
            admin.setName(request.getName());
        }

        if (request.getNickName() != null) {
            admin.setNickName(request.getNickName());
        }

        if (request.getRole() != null) {
            checkRoles(admin, request);
            admin.setRole(request.getRole());
        }

        adminRepository.save(admin);
        return new EditAdminResponse(adminId);
    }

    @Override
    public ListAdminItemResponse getById(String adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new UserNotFoundException(Error.USER_NOT_FOUND));

        return converter(admin);
    }

    @Override
    @Transactional
    public DeletePhotoResponse deletePhoto(String photoId, String adminId) {
        photoRepository.deletePhotoByIdAndAdminId(photoId, adminId);
        return new DeletePhotoResponse(photoId, true);
    }

    @Override
    public LoginAdminResponse loginAdmin(LoginAdminRequest request) {
        String login = request.getLogin();
        String password = MD5Util.computeMD5(request.getPassword());

        Admin admin = adminRepository.findByLoginAndPassword(login, password)
                .orElseThrow(() -> new UserNotFoundException(Error.USER_NOT_FOUND));

        List<PhotoDTO> photoDTOList = photoRepository.findAllByAdminId(admin.getId()).stream()
                .map(photoMapper::mapToDTO)
                .toList();

        return new LoginAdminResponse(
                admin.getId(),
                jwtTokenManager.generateAdminToken(admin),
                admin.getName(),
                admin.getNickName(),
                photoDTOList,
                admin.getRole()
        );
    }

    @Transactional
    public DeleteAdminResponse deleteAdmin(String adminId) {
        log.info("Deleting admin with ID: {}", adminId);

        Admin deletedAdmin = findAdminById(adminId);
        List<Admin> activeAdmins = findActiveAdminsExcluding(adminId);
        List<Bot2> botsToDelete = findAdminBots(adminId);

        reassignBots(activeAdmins, botsToDelete);

        markAdminAsDeleted(deletedAdmin);

        log.info("Admin {} marked as deleted.", adminId);

        return new DeleteAdminResponse(adminId);
    }

    private void checkRoles(Admin admin, EditAdminRequest request) {
        String adminId = admin.getId();
        if (admin.getRole() == 512 && request.getRole() == 256) {
            List<Admin> activeAdmins = findActiveAdminsExcluding(adminId);
            List<Bot2> botsToDelete = findAdminBots(adminId);
            reassignBots(activeAdmins, botsToDelete);
        }
    }

    private void reassignBots(List<Admin> activeAdmins, List<Bot2> botsToDelete) {
        int numberOfAdmins = activeAdmins.size();
        int botsPerAdmin = botsToDelete.size() / numberOfAdmins;
        int remainder = botsToDelete.size() % numberOfAdmins;

        log.info("Reassigning bots. Total bots: {}, Bots per admin: {}, Remainder: {}",
                botsToDelete.size(), botsPerAdmin, remainder);

        int botIndex = 0;
        int adminIndex = 0;

        for (Bot2 bot2 : botsToDelete) {
            Admin newAdmin = activeAdmins.get(adminIndex);
            String newAdminId = newAdmin.getId();
            List<Bot1> bot1List = getBot1ListForAdmin(newAdminId);

            for (Bot1 bot1 : bot1List) {
                bot2.setBot1(bot1);
            }

            bot2.setAdmin(newAdmin);
            bot2Repository.save(bot2);

            log.debug("Reassigned bot with ID {} to admin with ID {}.", bot2.getId(), newAdmin.getId());

            adminIndex = (adminIndex + 1) % numberOfAdmins;

            if (botIndex < remainder) {
                adminIndex = botIndex % numberOfAdmins;
                botIndex++;
            }
        }

        log.info("All bots reassigned.");
    }

    private void markAdminAsDeleted(Admin deletedAdmin) {
        UUID randomUUID = UUID.randomUUID();
        deletedAdmin.setDeleted(true);
        deletedAdmin.setLogin(randomUUID + DELETED);
        deletedAdmin.setNickName(randomUUID + DELETED);
        deletedAdmin.setRole(0);
        bot1Repository.deleteAllByAdminId(deletedAdmin.getId());
        adminRepository.save(deletedAdmin);
    }

    private ListAdminItemResponse converter(Admin admin) {
        String adminId = admin.getId();
        List<Photo> allByAdminId = photoRepository.findAllByAdminId(adminId);
        List<PhotoDTO> photoDTOList = allByAdminId.stream()
                .map(photoMapper::mapToDTO)
                .toList();
        List<String> bot1Ids = new ArrayList<>();
        List<Bot1> bot1s = bot1Repository
                .findAllByAdminIdAndParentType(adminId, ParentType.valueOf(ParentType.ADMIN.getValue()));
        for (Bot1 bot1 : bot1s) {
            bot1Ids.add(bot1.getIdentity());
        }

        return ListAdminItemResponse.builder()
                .id(admin.getId())
                .login(admin.getLogin())
                .name(admin.getName())
                .tg(admin.getNickName())
                .authority(admin.getRole())
                .images(photoDTOList)
                .bot1Ids(bot1Ids)
                .build();
    }

    private List<Bot1> getBot1ListForAdmin(String adminId) {
        return bot1Repository
                .findAllByAdminId(adminId)
                .orElseThrow(() -> new EntityNotFoundException(Error.ENTITY_NOT_FOUND));
    }

    private Admin findAdminById(String adminId) {
        return adminRepository.findById(adminId)
                .orElseThrow(() -> new EntityNotFoundException(Error.ENTITY_NOT_FOUND));
    }

    private List<Admin> findActiveAdminsExcluding(String adminId) {
        return adminRepository
                .findAllByRoleAndIsDeletedIsFalseAndNotExcluded(512, adminId);
    }

    private List<Bot2> findAdminBots(String adminId) {
        return bot2Repository.findAllByAdminId(adminId);
    }

}
