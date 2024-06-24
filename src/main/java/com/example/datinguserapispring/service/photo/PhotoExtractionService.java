package com.example.datinguserapispring.service.photo;

import com.example.datinguserapispring.dto.photo.response.UploadPhotoResponse;
import com.example.datinguserapispring.model.entity.Admin;
import com.example.datinguserapispring.model.entity.Photo;
import com.example.datinguserapispring.model.entity.bot.Bot2;
import com.example.datinguserapispring.model.entity.user.User;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface PhotoExtractionService {

    UploadPhotoResponse photoExtractorUser(String parentType,
                                       MultipartFile filePart,
                                       Path filePath,
                                       User user);

    UploadPhotoResponse photoExtractorBot2(String parentType,
                                           MultipartFile filePart,
                                           byte photoIndex,
                                           Path filePath,
                                           Bot2 bot2);


    UploadPhotoResponse photoExtractorAdmin(String parentType,
                                            MultipartFile filePart,
                                            byte photoIndex,
                                            Path filePath,
                                            Admin admin);

    UploadPhotoResponse builtPhotoResponse(Photo photo);

    UploadPhotoResponse photoExtractorChat(String parentType, Path filePath, MultipartFile filePart);
}
