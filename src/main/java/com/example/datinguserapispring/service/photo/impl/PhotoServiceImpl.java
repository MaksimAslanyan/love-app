package com.example.datinguserapispring.service.photo.impl;

import com.example.datinguserapispring.dto.photo.response.DeletePhotoResponse;
import com.example.datinguserapispring.dto.photo.response.PhotoListResponse;
import com.example.datinguserapispring.dto.photo.response.UploadPhotoResponse;
import com.example.datinguserapispring.exception.Bot2NotFoundException;
import com.example.datinguserapispring.exception.Error;
import com.example.datinguserapispring.exception.UserNotFoundException;
import com.example.datinguserapispring.mapper.PhotoMapper;
import com.example.datinguserapispring.model.entity.Admin;
import com.example.datinguserapispring.model.entity.Photo;
import com.example.datinguserapispring.model.entity.bot.Bot2;
import com.example.datinguserapispring.model.entity.user.User;
import com.example.datinguserapispring.repository.AdminRepository;
import com.example.datinguserapispring.repository.Bot2Repository;
import com.example.datinguserapispring.repository.PhotoRepository;
import com.example.datinguserapispring.repository.UserRepository;
import com.example.datinguserapispring.security.SecurityContextService;
import com.example.datinguserapispring.service.photo.PhotoExtractionService;
import com.example.datinguserapispring.service.photo.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;
    private final Bot2Repository bot2Repository;
    private final AdminRepository adminRepository;
    private final SecurityContextService securityContextService;
    private final PhotoMapper photoMapper;
    private final UserRepository userRepository;
    private final PhotoExtractionService photoExtractionService;
    @Value("${storage.path}")
    private String path;


    @Override
    public byte[] getPhoto(String photoId) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("No photo found with id " + photoId));

        try {
            return Files.readAllBytes(getRealPath(photo.getUrl()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read photo file", e);
        }
    }

    @Override
    public UploadPhotoResponse uploadBotPhoto(String id, String parentType,
                                              MultipartFile filePart, byte photoIndex) {
        Path filePath = resolvePath(filePart.getOriginalFilename(), parentType);
        Bot2 bot2 = bot2Repository.findById(id)
                .orElseThrow(() -> new Bot2NotFoundException(Error.BOT2_NOT_FOUND));

        return photoExtractionService.photoExtractorBot2(parentType, filePart, photoIndex, filePath, bot2);
    }

    @Override
    public UploadPhotoResponse uploadPhoto(String id, String parentType,
                                           MultipartFile filePart) {
        Path filePath = resolvePath(filePart.getOriginalFilename(), parentType);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(Error.USER_NOT_FOUND));
        return photoExtractionService.photoExtractorUser(parentType, filePart, filePath, user);
    }

    @Override
    public UploadPhotoResponse uploadAdminPhoto(String id, String parentType,
                                                MultipartFile filePart, byte photoIndex) {
        Path filePath = resolvePath(filePart.getOriginalFilename(), parentType);
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(Error.USER_NOT_FOUND));
        return photoExtractionService.photoExtractorAdmin(parentType, filePart, photoIndex, filePath, admin);

    }

    @Override
    public UploadPhotoResponse uploadPhotoInChat(String id, String parentType, MultipartFile filePart) {
        Path filePath = resolvePath(filePart.getOriginalFilename(), parentType);
        return photoExtractionService.photoExtractorChat(parentType, filePath, filePart);
    }

    @Override
    @Transactional
    public DeletePhotoResponse deleteBot2Photo(String photoId, String botId) {
        Photo photo = photoRepository.findById(photoId).get();
        photoRepository.deleteAllByUrlContaining(photo.getUrl());
        deleteFile(photo.getUrl());
        return new DeletePhotoResponse(photoId, true);
    }

    @Override
    public int countAllByUserId(String userId) {
        return photoRepository.countAllByUserId(userId);
    }

    @Override
    public PhotoListResponse myPhotos(String userName) {
        return photoMapper
                .mapToPhotoListResponseFromPhotos(photoRepository.findAllByUserName(userName));
    }

    @Transactional
    public DeletePhotoResponse deleteAdminPhoto(String photoId) {
        String login = securityContextService.getUserDetails().getUsername();
        Admin admin = adminRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(Error.USER_NOT_FOUND));
        List<Photo> allByAdminId = photoRepository.findAllByAdminId(admin.getId());

        log.info("Find the photo to delete: {}", photoId);
        Photo photoToDelete = null;
        for (Photo photo : allByAdminId) {
            if (photo.getId().equals(photoId)) {
                photoToDelete = photo;
                break;
            }
        }

        if (photoToDelete != null) {
            int indexToDelete = photoToDelete.getIsAvatar();

            log.info("Remove the photo from the list: {}", photoId);
            allByAdminId.remove(photoToDelete);

            log.info("Update the indices of the remaining photos");
            for (Photo photo : allByAdminId) {
                if (photo.getIsAvatar() > indexToDelete) {
                    photo.setIsAvatar((byte) (photo.getIsAvatar() - 1));
                    log.info("Save the updated photo to the repository");

                    photoRepository.save(photo);
                }
            }

            log.info("Delete the photo from the repository");
            photoRepository.deleteById(photoId);
        }

        return new DeletePhotoResponse(photoId, true);
    }

    private Path resolvePath(String name, String parentType) {
        LocalDateTime localDateTime = LocalDateTime.now();
        Path dirPath = Paths.get(path);
        Path dir = Paths.get(
                parentType,
                String.valueOf(localDateTime.getYear()),
                String.valueOf(localDateTime.getMonthValue()),
                String.valueOf(localDateTime.getDayOfMonth())
        );
        Path saveDir = dirPath.resolve(dir);

        try {
            if (!Files.exists(saveDir)) {
                Files.createDirectories(saveDir);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directories for saving photo", e);
        }

        return Paths.get(
                path,
                parentType,
                String.valueOf(localDateTime.getYear()),
                String.valueOf(localDateTime.getMonthValue()),
                String.valueOf(localDateTime.getDayOfMonth()),
                name
        );
    }

    private Path getRealPath(String uri) {
        return Paths.get(uri);
    }

    private void deleteFile(String filePathToDelete) {
        Path fullPath = Path.of(filePathToDelete);

        try {
            Files.deleteIfExists(fullPath);
            log.info("File deleted successfully: " + fullPath);

            adjustFileNames(filePathToDelete);
        } catch (IOException e) {
            log.info("Failed to delete file: " + fullPath);
            e.printStackTrace();
        }
    }

    private void adjustFileNames(String deletedFilePath) {
        Path parentPath = Path.of(Path.of(deletedFilePath).getParent().toString());

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(parentPath)) {
            List<Path> files = new ArrayList<>();
            for (Path path : directoryStream) {
                files.add(path);
            }

            files.sort((path1, path2) -> path1.compareTo(path2)); // Sort files by name

            for (int i = 0; i < files.size(); i++) {
                Path oldPath = files.get(i);
                Path newPath = parentPath.resolve(i + 1 + ".jpg"); // Adjust the new name as needed
                Files.move(oldPath, newPath);
            }

            log.error("File names adjusted successfully.");
        } catch (IOException e) {
            log.error("Failed to adjust file names.");
            e.printStackTrace();
        }
    }
}

