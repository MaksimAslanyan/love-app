package com.example.datinguserapispring.api.controller;


import com.example.datinguserapispring.constants.UserType;
import com.example.datinguserapispring.dto.photo.response.DeletePhotoResponse;
import com.example.datinguserapispring.dto.photo.response.PhotoListResponse;
import com.example.datinguserapispring.dto.photo.response.UploadPhotoResponse;
import com.example.datinguserapispring.security.AppUserDetails;
import com.example.datinguserapispring.security.SecurityContextService;
import com.example.datinguserapispring.service.photo.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;
    private final SecurityContextService securityContextService;


    @PostMapping(value = "/photo")
    public UploadPhotoResponse uploadPhoto(@RequestParam MultipartFile photo) {
        AppUserDetails appUserDetails = securityContextService.getUserDetails();
        return photoService.uploadPhoto(appUserDetails.getUsername(), UserType.USER, photo);
    }

    @PostMapping(value = "/chats/upload-photo")
    public UploadPhotoResponse uploadPhotoInChat(@RequestParam MultipartFile photo) {
        AppUserDetails appUserDetails = securityContextService.getUserDetails();
        return photoService.uploadPhotoInChat(appUserDetails.getUsername(), "CHAT", photo);
    }

    @GetMapping(value = "/image/{photoId}",
            produces = {"application/octet-stream", "image/jpeg", "image/png"})
    public byte[] getImage(@PathVariable("photoId") String photoId) {
        return photoService.getPhoto(photoId);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/my-images")
    public PhotoListResponse myImages() {
        AppUserDetails appUserDetails = securityContextService.getUserDetails();
        return photoService.myPhotos(appUserDetails.getUsername());
    }

    @DeleteMapping("/admin/image")
    public DeletePhotoResponse deletePhoto(@RequestBody String photoId) {
        return photoService.deleteAdminPhoto(photoId);
    }
}
