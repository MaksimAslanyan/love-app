package com.example.datinguserapispring.service.photo;

import com.example.datinguserapispring.dto.photo.response.DeletePhotoResponse;
import com.example.datinguserapispring.dto.photo.response.PhotoListResponse;
import com.example.datinguserapispring.dto.photo.response.UploadPhotoResponse;
import org.springframework.web.multipart.MultipartFile;

public interface PhotoService {
    UploadPhotoResponse uploadPhoto(String userDetails, String parentType,
                                    MultipartFile filePart);

    DeletePhotoResponse deleteAdminPhoto(String photoId);

    PhotoListResponse myPhotos(String userName);

    byte[] getPhoto(String photoId);

    UploadPhotoResponse uploadBotPhoto(String id, String bot, MultipartFile photo, byte photoIndex);

    DeletePhotoResponse deleteBot2Photo(String photoId, String botId);

    UploadPhotoResponse uploadAdminPhoto(String id, String parentType,
                                         MultipartFile filePart, byte photoIndex);

    int countAllByUserId(String userId);

    UploadPhotoResponse uploadPhotoInChat(String username, String user, MultipartFile photo);
}
