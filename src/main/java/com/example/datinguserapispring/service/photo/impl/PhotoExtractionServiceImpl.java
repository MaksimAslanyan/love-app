package com.example.datinguserapispring.service.photo.impl;

import com.example.datinguserapispring.dto.photo.response.UploadPhotoResponse;
import com.example.datinguserapispring.exception.Error;
import com.example.datinguserapispring.exception.PhotoUploadException;
import com.example.datinguserapispring.model.entity.Admin;
import com.example.datinguserapispring.model.entity.Photo;
import com.example.datinguserapispring.model.entity.bot.Bot2;
import com.example.datinguserapispring.model.entity.user.User;
import com.example.datinguserapispring.repository.PhotoRepository;
import com.example.datinguserapispring.service.photo.AsyncUpdateBotPhotoService;
import com.example.datinguserapispring.service.photo.PhotoExtractionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import static com.example.datinguserapispring.util.GetFilePathUtil.getFilePathUrlString;

@Service
@Slf4j
@RequiredArgsConstructor
public class PhotoExtractionServiceImpl implements PhotoExtractionService {

    private final PhotoRepository photoRepository;
    private final AsyncUpdateBotPhotoService asyncUpdateBotPhotoService;


    @Override
    public UploadPhotoResponse photoExtractorUser(String parentType,
                                                  MultipartFile filePart,
                                                  Path filePath,
                                                  User user) {
        try {
            String url = getFilePathUrlString(filePart, filePath);
            int photoSize = photoRepository.findAllByUserId(user.getId()).size();
            Photo photo;
            if (photoSize == 0) {
                byte photoIndex = 0;
                photo = new Photo(parentType, photoIndex, url, LocalDateTime.now(), user, false);
            } else {
                int index = photoSize + 1;
                photo = new Photo(parentType, (byte) index, url, LocalDateTime.now(), user, false);
            }
            return builtPhotoResponse(photo);
        } catch (IOException e) {
            throw new PhotoUploadException(Error.FAILED_UPLOAD_PHOTO);
        }
    }

    @Transactional
    @Override
    public UploadPhotoResponse photoExtractorBot2(String parentType,
                                                  MultipartFile filePart,
                                                  byte photoIndex,
                                                  Path filePath,
                                                  Bot2 bot2) {
        try {
            String url = savePhotoAndGetUrl(filePart, Path.of(bot2.getPackagePath()));
            Photo photo = new Photo(parentType, photoIndex, url, LocalDateTime.now(), bot2);
            asyncUpdateBotPhotoService.updatePhotos(bot2, url);

            return builtPhotoResponse(photo);
        } catch (IOException e) {
            throw new PhotoUploadException(Error.FAILED_UPLOAD_PHOTO);
        }
    }

    @Override
    public UploadPhotoResponse photoExtractorAdmin(String parentType,
                                                   MultipartFile filePart,
                                                   byte photoIndex,
                                                   Path filePath,
                                                   Admin admin) {
        try {
            String url = getFilePathUrlString(filePart, filePath);
            Photo photo = new Photo(parentType, photoIndex, url, LocalDateTime.now(), admin);
            return builtPhotoResponse(photo);
        } catch (IOException e) {
            throw new PhotoUploadException(Error.FAILED_UPLOAD_PHOTO);
        }
    }

    @Override
    public UploadPhotoResponse photoExtractorChat(String parentType, Path filePath, MultipartFile filePart) {
        try {
            String url = getFilePathUrlString(filePart, filePath);
            Photo photo = new Photo(parentType, url, LocalDateTime.now());
            return builtPhotoResponse(photo);
        } catch (IOException e) {
            throw new PhotoUploadException(Error.FAILED_UPLOAD_PHOTO);
        }

    }

    public UploadPhotoResponse builtPhotoResponse(Photo photo) {
        Photo savedPhoto = photoRepository.save(photo);

        String filePath = photo.getUrl();
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance()
                .scheme("file") // Use the "file" scheme for local file paths
                .path(filePath);

        URI uri = builder.build().toUri();
        String photoId = savedPhoto.getId();


        return new UploadPhotoResponse(uri, photoId);
    }

    private String savePhotoAndGetUrl(MultipartFile filePart, Path filePath) throws IOException {
        String uniqueFilename = getNewPhotoIndex(filePath);

        Path completePath = filePath.resolve(uniqueFilename);

        try (OutputStream outputStream = Files.newOutputStream(completePath)) {
            outputStream.write(filePart.getBytes());
        }

        return completePath.toString();
    }

    private String getNewPhotoIndex(Path packagePath) {
        for (byte i = 1; i <= 6; i++) {
            Path photoPath = packagePath.resolve(i + ".jpg");
            if (!Files.exists(photoPath)) {
                return i + ".jpg";
            }
        }
        return "7";
    }
}
